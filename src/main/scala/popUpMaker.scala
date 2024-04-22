import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ColorPicker, Label, Spinner, TextField}
import scalafx.scene.layout.{GridPane, HBox, Pane, VBox}
import scalafx.scene.shape.{Arc, ArcType, Circle, Ellipse, Rectangle}
import scalafx.scene.transform.Rotate
import scalafx.stage.Stage
import scalafx.scene.SceneIncludes.jfxColor2sfx
import scalafx.scene.image.ImageView

import scala.collection.mutable.ListBuffer

var w = 0.0
var h = 0.0

/** Metodi, joka tuottaa popUp ikkunan joka mahdollistaa huonekalun ominaisuuksien muuttamisen */
def popUpMaker(n: Furniture, furnitureIsIn: Pane, listOfFurniture: ListBuffer[Furniture] ): Stage =

  val stage = new Stage()
  stage.setX( 300 )
  stage.setY( 300 )

  /** Tarkistetaan, onko huonekalun leveys vai pituus suurempi */
  val bigger =
    if n.width < n.lenght then
      n.lenght
    else
      n.width

 /** Luodaan uusi GradPane, johon lisätään kaikki popUpin elementit */
  val grid = new GridPane():
    hgap = 10
    vgap = 10
    padding = Insets(10, 10, 10, 10)

  /** Paneeli, jossa säilytetään huonekalun muutokseen liittyviä kenttiä */
  val changePanel = new VBox():
    spacing = 20

  /** Paneeli, joka säilytää submit ja delete napit */
  val submitOrDelete = new HBox():
    spacing = 20
    padding = Insets(20, 20, 20, 20)

  /***/
  val label1 = new Label("Modify this piece of furniture")

  /** Kopioidaan huonekalu ja asetetaan sille kaikki alkuperäisen huonekalun ominaisuudet */
  val copyOfFurniture = n.copy()
  copyOfFurniture.changeColor( n.color )
  val shape = copyOfFurniture.shape
  shape.getTransforms.addAll( n.shape.getTransforms )

  val colorPicker = ColorPicker( n.color )
  colorPicker.onAction = (event) => copyOfFurniture.changeColor(colorPicker.getValue)

  /** Paneeli, joka säilyttää kopion */
  val shapePanel = new VBox():
    val a = bigger / 2
    padding = Insets( a, a ,a, a )


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
      changePanel.children = Array( colorPicker, scalingWidth)
    case shape: Arc =>
      changePanel.children = Array( rotateButton, scalingWidth, scalingHeight)
    case _ =>
      changePanel.children = Array( colorPicker, rotateButton, scalingWidth, scalingHeight)

  val image = new ImageView(n.image)
  image.fitWidth = 300
  image.fitHeight = 300

  shapePanel.children = Array( shape)
  submitOrDelete.children = Array( submitButton, deleteButton )
  grid.add(label1, 2, 0, 2, 1)
  grid.add(shape, 3, 2)
  grid.add(submitOrDelete, 2, 5,3, 1)
  grid.add(changePanel, 4, 2, 3, 1)
  grid.add( image, 0, 0, 2, 7 )

  val scene = new Scene(grid)

  stage.setScene(scene)

  stage
