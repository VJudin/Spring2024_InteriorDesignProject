import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ColorPicker, Label}
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.scene.shape.Rectangle
import scalafx.scene.transform.Rotate
import scalafx.stage.Stage
import scalafx.scene.SceneIncludes.jfxColor2sfx

import scala.collection.mutable.ListBuffer

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
