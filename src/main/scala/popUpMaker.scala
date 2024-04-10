import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ColorPicker, Label}
import scalafx.scene.layout.{GridPane, HBox, Pane, VBox}
import scalafx.scene.shape.Rectangle
import scalafx.scene.transform.Rotate
import scalafx.stage.Stage
import scalafx.scene.SceneIncludes.jfxColor2sfx

import scala.collection.mutable.ListBuffer

/** Metodi, joka tuottaa popUp ikkunan huonekalun värin vaihtamiseksi */
def popUpMaker(n: Furniture, furnitureIsIn: Pane, listOfFurniture: ListBuffer[Furniture] ): Stage =

  val stage = new Stage()
  stage.setHeight(500 )
  stage.setWidth(500 )
  stage.setX( 300 )
  stage.setY( 300 )

  val bigger =
    if n.width < n.lenght then
      n.lenght
    else
      n.width

  val grid = new GridPane():
    hgap = 10
    vgap = 10
    padding = Insets(10, 10, 10, 10)
    prefWidth = stage.width.toDouble
    prefHeight = stage.height.toDouble


  val pane = new VBox():
    margin = Insets(10)
    spacing = 10
    padding = Insets(10, 10, 10, bigger )

  val changables = new HBox():
    spacing = 20

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
  shape.scaleX = 0.75
  shape.scaleY = 0.75
  shape.getTransforms.addAll( n.shape.getTransforms )

  val shapePanel = new VBox():
      if n.width < n.lenght then
        minHeight = n.lenght
        minWidth = n.lenght
        padding = Insets( 20, 20 ,20, 20 )
      else
        minHeight = n.width
        minWidth = n.width
        padding = Insets( 20, 20, 20, 20 )

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

 /** shapePanel.children = Array( shape )
  changePanel.children = Array(colorPicker, rotateButton)
  changables.children = Array( shapePanel, changePanel )
  submitOrDelete.children = Array( submitButton, deleteButton )
  pane.children = Array(label1, changables, submitOrDelete ) */
  submitOrDelete.children = Array( submitButton, deleteButton )
  grid.add(label1, 0, 0)
  grid.add(shape, 0, 2)
  grid.add(submitOrDelete, 0, 5,3, 1)
  grid.add(colorPicker, 1, 1, 3, 1)
  grid.add(rotateButton, 1, 2, 3, 1)

  val scene = new Scene(grid)

  stage.setScene(scene)

  stage
