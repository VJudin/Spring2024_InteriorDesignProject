import DesingGUI.floorPlanScaleX
import javafx.geometry
import javafx.scene.canvas
import javafx.scene.input.MouseButton
import scalafx.geometry.{Insets, Point2D}
import scalafx.scene.control.{Button, ColorPicker, Label}
import scalafx.scene.layout.{Background, GridPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color.{Blue, Green, Pink, Red, White}
import scalafx.scene.shape.{Circle, Ellipse, Rectangle, Shape}
import scalafx.scene.text.Font
import scalafx.geometry
import scalafx.scene.{Node, Scene}
import scalafx.scene.shape.Shape.{intersect, sfxShape2jfx}
import scalafx.scene.SceneIncludes.jfxColor2sfx
import scalafx.scene.transform.Rotate
import scalafx.stage.{Popup, Stage}

import scala.collection.mutable.ListBuffer


class FurniturePanel (f: Furniture, givenWidth: Double, givenHeight: Double, addTo: Pane, listOfFurniture: ListBuffer[Furniture], var xscale: Double, var yscale: Double ) extends VBox:


  this.prefHeight = givenHeight
  this.prefWidth = givenWidth

  val panel = this

  background = Background.fill(White)
  
  f.shape.fill = f.color
  val furnitureName = new Label(f.fname):
    font = Font(14)

  spacing = 6
  padding = Insets(20, 20, 20, 20)

  var addButton = new Button(s"Add new ${f.fname}"):
    onAction = (event) =>
      var newOne = f.copy()
      val shape = newOne.shape
      shape.setScaleX( DesingGUI.floorPlanScaleX )
      shape.setScaleY(DesingGUI.floorPlanScaleY)
      newOne.x = addTo.prefWidth.toDouble / 2
      newOne.y = addTo.prefHeight.toDouble / 2
      addTo.children += shape
      shape.setLayoutX( newOne.x )
      shape.setLayoutY( newOne.y )
      val draggableMaker = new DraggableMaker()
      draggableMaker.makeDraggable( newOne, listOfFurniture )
      newOne.shape.onMouseClicked  = (event) =>
        if event.getButton == MouseButton.SECONDARY then
          popUpMaker(newOne, addTo, listOfFurniture).show()
      listOfFurniture += newOne

  this.children = Array( furnitureName, f.shape, addButton )


/** Tekee huonekaluista liikuteltavia */
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

  def makeDraggable( a: Furniture, b: ListBuffer[Furniture] ) =
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
      else
        n.setLayoutX( event.getSceneX + mouseOffsetFromNodeX)
        n.setLayoutY( event.getSceneY + mouseOffsetFromNodeY)
        a.x = event.getSceneX + mouseOffsetFromNodeX
        a.y = event.getSceneY + mouseOffsetFromNodeY
      n.setTranslateX(0)
      n.setTranslateY(0))


/** Metodi, joka tuottaa popUp ikkunan huonekalun värin vaihtamiseksi */

def popUpMaker(n: Furniture, furnitureIsIn: Pane, listOfFurniture: ListBuffer[Furniture] ): Stage =


  val stage = new Stage()
  if n.width < n.lenght then
    stage.setHeight(200 + n.lenght)
    stage.setWidth(300 + n.lenght)
  else
    stage.setHeight( 200 + n.width )
    stage.setWidth(300 + n.width)
  stage.setX( 300 )
  stage.setY( 300 )

  val pane = new VBox():
    margin = Insets(10)
    spacing = 10
    padding = Insets(10, 10, 10, 10)

  val changables = new HBox():
    spacing = 150

  val changePanel = new VBox():
    prefHeight = stage.height.toDouble
    prefWidth = 400
    spacing = 20

  val submitOrDelete = new HBox():
    spacing = 20
    padding = Insets(20, 20, 20, 20)


  val label1 = new Label("Modify this piece of furniture")
  val colorPicker = ColorPicker( n.color )
  val copyOfFurniture = n.copy()
  val shape = copyOfFurniture.shape
  shape.fill = n.color
  shape.getTransforms.addAll( n.shape.getTransforms )


  val shapePanel = new VBox():
      if n.width < n.lenght then
        minHeight = n.lenght
        minWidth = n.lenght
        padding = Insets( n.lenght / 2, n.lenght/4 , n.lenght / 4, 20 )
      else
        minHeight = n.width
        minWidth = n.width
        padding = Insets( n.width / 2, n.width / 4, n.width / 4, 20 )

  colorPicker.onAction = (event) => shape.fill = colorPicker.getValue

  val submitButton = new Button( "Submit changes"):
    onAction = (event) =>
      n.changeColor( colorPicker.getValue )
      n.shape.getTransforms.clear()
      n.shape.getTransforms.addAll( copyOfFurniture.shape.getTransforms )
      stage.close()

  val deleteButton = new Button( "Delete this furniture"):
    onAction = (event) =>
      var indexOf = furnitureIsIn.children.indexOf(n.shape)
      furnitureIsIn.getChildren.remove( indexOf )
      listOfFurniture.remove(indexOf)
      stage.close()

  val rotateButton = new Button( "Rotate this furniture"):
    onAction = (event) =>
      n.shape match
        case shape: Rectangle =>
          copyOfFurniture.shape.getTransforms.add( new Rotate( 45, n.width / 2, n.lenght / 2 ))
        case _ =>
          copyOfFurniture.shape.getTransforms.add( new Rotate( 45, 0, 0 ) )

  shapePanel.children = Array( shape )
  changePanel.children = Array(colorPicker, rotateButton)
  changables.children = Array( shapePanel, changePanel )
  submitOrDelete.children = Array( submitButton, deleteButton )
  pane.children = Array(label1, changables, submitOrDelete )

  val scene = new Scene(pane)

  stage.setScene(scene)

  stage