import scalafx.scene.layout.Pane
import scalafx.scene.shape.Shape

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
  def checkIntersection(f: Furniture, l: ListBuffer[Furniture] ): Boolean =
    /** Bufferi, joka kerää kaikki arvot huonekalun kohdasta verrattuna muihin huonekaluihin */
    val collisions = ListBuffer[Boolean]()
    /** Filteröidään huonekalujen listasta pois tarkasteltava huonekalu */
    var d = l.filter(x => !x.equals(f))
    if d.nonEmpty then
      for furniture <- d do
    /** Jos huonekalun päälle voidaan laittaa toinen huonekalu ja kyseinen huonekalu voi olla toisen huonekalun päällä
      * ei tarvitse huomioida huonekalujen päällekkäisyyttä */
        if (f.canBePlacedOnTop && furniture.canHaveOnTop) || (f.canHaveOnTop && furniture.canBePlacedOnTop) then
          collisions += false
        else if f.fname == "Wall" && furniture.fname == "Wall" then
          collisions += false
        else
          if !f.shape.equals(furniture.shape) then
            var intersect = Shape.intersect( furniture.shape, f.shape )
            if intersect.getBoundsInLocal.getWidth != -1 then
             collisions += true
    collisions.contains( true )

  /** Tarkistaa, että huonekalu on käyttöliittymän näkymässä vertaamalla huonekalun ja Panen boundseja. */
  def isOutOfBounds(f: Furniture, p: Pane): Boolean =
    val boundsOfShape = f.shape.getBoundsInParent
    val boundsOfPane = p.getBoundsInParent
    if boundsOfShape.getMinY > boundsOfPane.getMinY && boundsOfShape.getMinX > boundsOfPane.getMinX && boundsOfShape.getMaxX < boundsOfPane.getMaxX && boundsOfShape.getMaxY < boundsOfPane.getMaxY then
      false
    else
      true


  /** Metodi, joka tekee huonekalusta liikutettavan */
  def makeDraggable(f: Furniture, l: ListBuffer[Furniture], p: Pane): Unit =

    /** Tallennetaan muuttujaan liikutettava muoto */
    var n = f.shape
    var e = f.shape.getBoundsInParent

    /** Kun muotoa klikataan, alustetaan tietyt tiedot sen liikuttamista ja myöhempiä metodeja varten */
    n.setOnMousePressed((event) =>
      mouseAnchorX = event.getSceneX
      mouseAnchorY = event.getSceneY
      mouseOffsetFromNodeX = f.x - mouseAnchorX
      mouseOffsetFromNodeY = f.y - mouseAnchorY
      priorPositionX = n.getLayoutX
      priorPositionY = n.getLayoutY)

    /** Kun kuviota raahataan hiirellä se liikkuu hiiren mukana */
    n.setOnMouseDragged((event) =>
      n.setTranslateX(event.getSceneX - mouseAnchorX)
      n.setTranslateY(event.getSceneY - mouseAnchorY)
    )

    /** Kun kuviosta päästetään irti suoritetaan tarkistukset,
     * mikäli huonekalu on jonkin toisen huonekalun päällä tai jokin osa siitä on
     * käyttöliittymän näkymän ulkopuolella, palautetaan kuvio alkuperäisiin koordinaatteihinsa */
    n.setOnMouseReleased((event) =>
      if checkIntersection(f, l) then
        n.setLayoutX(priorPositionX)
        n.setLayoutY(priorPositionY)
      else if isOutOfBounds(f, p) then
        n.setLayoutX(priorPositionX)
        n.setLayoutY(priorPositionY)
      else
        n.setLayoutX(event.getSceneX + mouseOffsetFromNodeX)
        n.setLayoutY(event.getSceneY + mouseOffsetFromNodeY)
        f.x = event.getSceneX + mouseOffsetFromNodeX
        f.y = event.getSceneY + mouseOffsetFromNodeY
      n.setTranslateX(0)
      n.setTranslateY(0))
  end makeDraggable


end DraggableMaker
