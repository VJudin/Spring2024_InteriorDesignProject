import scalafx.scene.layout.Pane
import scalafx.scene.shape.Shape

import scala.collection.mutable.ListBuffer

class DraggableMaker:

  /** The needed variables are stated */
  private var mouseAnchorX = 0.0
  private var mouseAnchorY = 0.0
  private var mouseOffsetFromNodeX = 0.0
  private var mouseOffsetFromNodeY = 0.0
  private var priorPositionX = 0.0
  private var priorPositionY = 0.0

  /** This method makes sure the furniture is not on top of another piece of
    * furniture
    */
  def checkIntersection(f: Furniture, l: ListBuffer[Furniture]): Boolean =
    /** Buffer that has all the values regarding the relative positions of the
      * furniture
      */
    val collisions = ListBuffer[Boolean]()

    /** Filter out the furniture that is looked at currently */
    var d = l.filter(x => !x.equals(f))
    if d.nonEmpty then
      for furniture <- d do
        /** if the furniture can have another on top of it and that furniture is
          * allowed to be on top of another piece of furniture then their
          * collision is not taken into consideration
          */
        if (f.canBePlacedOnTop && furniture.canHaveOnTop) || (f.canHaveOnTop && furniture.canBePlacedOnTop)
        then collisions += false
        /** If both of the furniture are walls then we don't consider a
          * collition
          */
        else if f.fname == "Wall" && furniture.fname == "Wall" then
          collisions += false
        else if !f.shape.equals(furniture.shape) then
          /** An intersect of the two shapes is made, if the intersect is bigger
            * than -1, the furniture are on top of one another and a true value
            * is added to the buffer
            */
          var intersect = Shape.intersect(furniture.shape, f.shape)
          if intersect.getBoundsInLocal.getWidth > -1 then collisions += true

    /** If a true value was found during the check, the furniture is on top of
      * another furniture and the function returns True, else it returns False.
      */
    collisions.contains(true)
  end checkIntersection

  /** This method makes sure the that the furniture stays in the GUI and can't
    * be dragged outside of it. Compares the bounds of the furniture and Pane
    */
  def isOutOfBounds(f: Furniture, p: Pane): Boolean =
    val boundsOfShape = f.shape.getBoundsInParent
    val boundsOfPane = p.getBoundsInParent
    if boundsOfShape.getMinY > boundsOfPane.getMinY && boundsOfShape.getMinX > boundsOfPane.getMinX && boundsOfShape.getMaxX < boundsOfPane.getMaxX && boundsOfShape.getMaxY < boundsOfPane.getMaxY
    then false
    else true
  end isOutOfBounds

  /** Makes the furniture draggable */
  def makeDraggable(f: Furniture, l: ListBuffer[Furniture], p: Pane): Unit =

    /** Save the shape to a variable */
    var n = f.shape
    var e = f.shape.getBoundsInParent

    /** I used an Eden Coding tutorial for help with this part */
    /** When a furniture is clicked, it's original coordinates are set as well
      * as the distance from the center of the furniture so that the dragging is
      * as smooth as possible
      */
    n.setOnMousePressed((event) =>
      mouseAnchorX = event.getSceneX
      mouseAnchorY = event.getSceneY
      mouseOffsetFromNodeX = f.x - mouseAnchorX
      mouseOffsetFromNodeY = f.y - mouseAnchorY
      priorPositionX = n.getLayoutX
      priorPositionY = n.getLayoutY
    )

    /** The furniture moves with the mouse when it is dragged */
    n.setOnMouseDragged((event) =>
      n.setTranslateX(event.getSceneX - mouseAnchorX)
      n.setTranslateY(event.getSceneY - mouseAnchorY)
    )

    /** When the furniture is let go, it's checked weather the furniture is on
      * top of another piece of furniture or not. It is also checked weather or
      * not the furniture is within the bounds of the GUI. If the answer to
      * either of these is TRUE, the furniture is returned to it's original
      * coordinates
      */
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
      n.setTranslateY(0)
    )

  end makeDraggable

end DraggableMaker
