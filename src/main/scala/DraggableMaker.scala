import DesingGUI.floorPlanScale
import scalafx.scene.image.Image
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, Circle, Ellipse, Rectangle, Shape}

import java.io.FileInputStream
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
    /** Filteröidään huonekalujen listasta pois tarkasteltava huonekalu */
    var d = b.filter( x => !x.equals(s))
    if d.nonEmpty then
      for furniture <- d do
    /** Jos huonekalun päälle voidaan laittaa toinen huonekalu ja kyseinen huonekalu voi olla toisen huonekalun päällä
      * ei tarvitse huomioida huonekalujen päällekkäisyyttä */
        if s.canBePlacedOnTop && furniture.canHaveOnTop then
          collisionDetected = false
        else
          if !s.shape.equals(furniture.shape) then
            var intersect = Shape.intersect( furniture.shape, s.shape )
            if intersect.getBoundsInLocal.getWidth != -1 then
             collisionDetected = true
    collisionDetected


  /** Tarkistaa, että huonekalu on käyttöliittymän näkymässä vertaamalla huonekalun ja Panen boundseja. */
  def isOutOfBounds(s: Furniture, a: Pane): Boolean =
    val boundsOfShape = s.shape.getBoundsInParent
    val boundsOfPane = a.getBoundsInParent
    if boundsOfShape.getMinY > boundsOfPane.getMinY && boundsOfShape.getMinX > boundsOfPane.getMinX && boundsOfShape.getMaxX < boundsOfPane.getMaxX && boundsOfShape.getMaxY < boundsOfPane.getMaxY then
      false
    else
      true
     
      
  /** Metodi, joka tekee huonekalusta liikutettavan */
  def makeDraggable( a: Furniture, b: ListBuffer[Furniture], c: Pane ) =
    
  /** Tallennetaan muuttujaan liikutettava muoto */
    var n = a.shape

    /** Kun muotoa klikataan, alustetaan tietyt tiedot sen liikuttamista ja myöhempiä metodeja varten */
    n.setOnMousePressed((event) =>
      mouseAnchorX = event.getSceneX
      mouseAnchorY = event.getSceneY
      mouseOffsetFromNodeX = a.x - mouseAnchorX
      mouseOffsetFromNodeY = a.y - mouseAnchorY
      priorPositionX = n.getLayoutX
      priorPositionY = n.getLayoutY)

    /** Kun kuviota raahataan hiirellä se liikkuu hiiren mukana */
    n.setOnMouseDragged((event) =>
      n.setTranslateX(event.getSceneX - mouseAnchorX )
      n.setTranslateY(event.getSceneY - mouseAnchorY )
      )

    /** Kun kuviosta päästetään irti suoritetaan tarkistukset, 
     * mikäli huonekalu on jonkin toisen huonekalun päällä tai jokin osa siitä on 
     * käyttöliittymän näkymän ulkopuolella, palautetaan kuvio alkuperäisiin koordinaatteihinsa */
    n.setOnMouseReleased((event) =>
      if checkIntersection( a, b ) then
        n.setLayoutX( priorPositionX)
        n.setLayoutY( priorPositionY)
      else if isOutOfBounds( a, c) then
        n.setLayoutX( priorPositionX)
        n.setLayoutY( priorPositionY)
      else
        n.setLayoutX( event.getSceneX + mouseOffsetFromNodeX)
        n.setLayoutY( event.getSceneY + mouseOffsetFromNodeY)
        a.x = event.getSceneX + mouseOffsetFromNodeX
        a.y = event.getSceneY + mouseOffsetFromNodeY
      n.setTranslateX(0)
      n.setTranslateY(0))
    
    
/** def wallChecker( s: Furniture, from: Image ) =
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
      false*/