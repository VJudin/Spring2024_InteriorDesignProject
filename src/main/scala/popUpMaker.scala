import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ColorPicker, Label, Spinner, TextField}
import scalafx.scene.layout.{GridPane, HBox, Pane, VBox}
import scalafx.scene.shape.{Arc, ArcType, Circle, Ellipse, Rectangle}
import scalafx.scene.transform.Rotate
import scalafx.stage.Stage
import scalafx.scene.SceneIncludes.jfxColor2sfx

import scala.collection.mutable.ListBuffer

var w = 0.0
var h = 0.0

/** Metodi, joka tuottaa popUp ikkunan huonekalun värin vaihtamiseksi */
def popUpMaker(n: Furniture, furnitureIsIn: Pane, listOfFurniture: ListBuffer[Furniture] ): Stage =

  val stage = new Stage()
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


  /**val pane = new VBox():
    margin = Insets(10)
    spacing = 10
    padding = Insets(10, 10, 10, bigger )

  val changables = new HBox():
    spacing = 20 */

  val changePanel = new VBox():
    spacing = 20

  val submitOrDelete = new HBox():
    spacing = 20
    padding = Insets(20, 20, 20, 20)

  val label1 = new Label("Modify this piece of furniture")
  val colorPicker = ColorPicker( n.color )
  val copyOfFurniture = n.copy()
  copyOfFurniture.changeColor( n.color )
  val shape = copyOfFurniture.shape
  shape.scaleX = 0.75
  shape.scaleY = 0.75
  shape.getTransforms.addAll( n.shape.getTransforms )

  val shapePanel = new VBox():
      if n.width < n.lenght then
        val a = n.lenght / 2
        padding = Insets( a, a ,a, a )
      else
        val b = n.width / 2
        padding = Insets( b, b, b, b)

  colorPicker.onAction = (event) => copyOfFurniture.changeColor(colorPicker.getValue)

  val scaleWidth2 = new Spinner[Double](10.0, 500, n.width, 10.0)
  val scaleHeight2 = new Spinner[Double](10.0, 500, n.lenght, 10.0)
  val label2 = new Label("cm")
  val label3 = new Label("cm")
  val scalingWidth = new HBox():
    spacing = 5
    children = Array(scaleWidth2, label2)
  val scalingHeight = new HBox():
    spacing = 5
    children = Array(scaleHeight2, label3)


  val submitButton = new Button( "Submit changes"):
    onAction = (event) =>
      n.changeColor( colorPicker.getValue )
      n.shape.getTransforms.clear()
      n.shape.getTransforms.addAll( copyOfFurniture.shape.getTransforms )
      w = scaleWidth2.getValue
      h = scaleHeight2.getValue
      n.changeSize(w, h)
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
        case shape: Arc =>
          copyOfFurniture.shape.getTransforms.add( new Rotate( 45, n.width / 2, -n.width / 2  ))
        case _ =>
          copyOfFurniture.shape.getTransforms.add( new Rotate( 45, 0, 0 ) )


  n.shape match
    case shape: Circle =>
      changePanel.children = Array( colorPicker, rotateButton, scalingWidth)
    case shape: Arc =>
      changePanel.children = Array( rotateButton, scalingWidth, scalingHeight)
    case _ =>
      changePanel.children = Array( colorPicker, rotateButton, scalingWidth, scalingHeight)


  shapePanel.children = Array( shape)
  submitOrDelete.children = Array( submitButton, deleteButton )
  //changePanel.children = Array( colorPicker, rotateButton, scalingWidth, scalingHeight)
  grid.add(label1, 0, 0, 2, 1)
  grid.add(shape, 1, 2)
  grid.add(submitOrDelete, 0, 5,3, 1)
  grid.add(changePanel, 2, 2, 3, 1)

  val scene = new Scene(grid)

  stage.setScene(scene)

  stage
