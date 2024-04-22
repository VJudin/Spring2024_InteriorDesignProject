import DesingGUI.floorPlanScale
import scalafx.scene.image.Image
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, Circle, Ellipse, Rectangle, Shape}

import scala.collection.mutable.ListBuffer

class DraggableMaker:

  /** Alustetaan tarvittavat muuttujat */
  private var mouseAnchorX = 0.0
  private var mouseAnchorY = 0.0
  private var mouseOffsetFromNodeX = 0.0
  private var mouseOffsetFromNodeY = 0.0
  private var priorPositionX = 0.0
  private var priorPositionY = 0.0

  /** Tarkistaa, onko huonekalun kohdalla toista huonekalua, jonka päälle se ei voi mennä */
  def checkIntersection( s: Furniture, b: ListBuffer[Furniture] ) =
    var collisionDetected = false
    var d = b.filter( x => !x.equals(s))
    if d.nonEmpty then
      for furniture <- d do
        if s.canBePlacedOnTop && furniture.canHaveOnTop then
          collisionDetected = false
        else
          if !s.shape.equals(furniture.shape) then
            var intersect = Shape.intersect( furniture.shape, s.shape )
            if intersect.getBoundsInLocal.getWidth != -1 then
             collisionDetected = true
    collisionDetected


  /** Tarkistaa, että huonekalu on käyttöliittymän näkymässä */
  def isOutOfBounds(coordX: Double, coordY: Double, s: Furniture, a: Pane): Boolean =
    val parentBounds = a.getLayoutBounds
    val paneWidth = parentBounds.getMaxX
    val paneHeight = parentBounds.getMaxY
    var maxX = 0.0
    var maxY = 0.0
    var minX = 0.0
    var minY = 0.0
    val width = s.width * floorPlanScale
    val height = s.lenght * floorPlanScale
    val errorX = s.width - width
    val errorY = height - s.lenght
    s.shape match
      case shape: Rectangle =>
        maxX = paneWidth - s.width + errorX
        maxY = paneHeight - s.lenght - errorY
        minX = parentBounds.getMinX - errorX
        minY = parentBounds.getMinY + errorY
      case shape: Circle =>
        minX = width
        maxX = paneWidth - width
        minY = width
        maxY = paneHeight - width
      case  shape: Ellipse =>
        minX = width
        maxX = paneWidth - width
        minY = height
        maxY = paneHeight - height
      case shape: Arc =>
        maxX = paneWidth
        maxY = paneHeight
    if coordX <= maxX && coordX >= minX && coordY <= maxY && coordY >= minY then
      false
    else
      true
      
  def wallChecker( s: Furniture, where: Pane, from: Image, allFurniture: ListBuffer[Furniture] ) =
    val reader = from.getPixelReader
    val untilX = s.width.toInt
    val untilY = s.lenght.toInt
    var isOnWall = ListBuffer[Boolean]()
    for x <- s.x.toInt until untilX do
      for y <- s.y.toInt until untilY do
        if !reader.getColor(x, y).eq(Color.White) then
          isOnWall += true
    if isOnWall.nonEmpty then 
      true
    else 
      false
      
  /** Metodi, joka tekee huonekalusta liikutettavan */
  def makeDraggable( a: Furniture, b: ListBuffer[Furniture], c: Pane ) =
    
    var n = a.shape

    n.setOnMousePressed((event) =>
      mouseAnchorX = event.getSceneX
      mouseAnchorY = event.getSceneY
      mouseOffsetFromNodeX = a.x - mouseAnchorX
      mouseOffsetFromNodeY = a.y - mouseAnchorY
      priorPositionX = n.getLayoutX
      priorPositionY = n.getLayoutY)

    n.setOnMouseDragged((event) =>
      n.setTranslateX(event.getSceneX - mouseAnchorX )
      n.setTranslateY(event.getSceneY - mouseAnchorY )
      )

    n.setOnMouseReleased((event) =>
      if checkIntersection( a, b ) then
        n.setLayoutX( priorPositionX)
        n.setLayoutY( priorPositionY)
      else if isOutOfBounds(event.getSceneX + mouseOffsetFromNodeX, event.getSceneY + mouseOffsetFromNodeY, a, c) then
        n.setLayoutX( priorPositionX)
        n.setLayoutY( priorPositionY)
      else
        n.setLayoutX( event.getSceneX + mouseOffsetFromNodeX)
        n.setLayoutY( event.getSceneY + mouseOffsetFromNodeY)
        a.x = event.getSceneX + mouseOffsetFromNodeX
        a.y = event.getSceneY + mouseOffsetFromNodeY
      n.setTranslateX(0)
      n.setTranslateY(0))