import DesingGUI.floorPlanScale
import scalafx.scene.layout.Pane
import scalafx.scene.shape.Shape

import scala.collection.mutable.ListBuffer

class DraggableMaker:

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

  def isOutOfBounds(coordX: Double, coordY: Double, s: Furniture, a: Pane): Boolean =
    val maxX = a.getPrefWidth
    val maxY = a.getPrefHeight
    val minX = -20
    val minY = -20
    val width = s.width * floorPlanScale
    val height = s.lenght * floorPlanScale
    if coordX >= minX && coordY >= minY && coordX + width <= maxX && coordY + height <= maxY then
      false
    else
      true

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
