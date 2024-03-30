import javafx.scene.canvas
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, ColorPicker, Label}
import scalafx.scene.layout.{Background, GridPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color.{Blue, Green, White}
import scalafx.scene.shape.{Circle, Rectangle, Shape}
import scalafx.scene.text.Font
import scalafx.geometry
import scalafx.scene.Node
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.MouseEvent.MouseMoved
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.SceneIncludes.jfxColor2sfx

import java.awt.MouseInfo
import scala.collection.mutable.ListBuffer


class FurniturePanel (f: Furniture, givenWidth: Double, givenHeight: Double, addTo: Pane, listOfFurniture: ListBuffer[Furniture] ) extends VBox:


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
      //addTo.add(shape, newOne.x, newOne.y)
      addTo.children += shape
      val draggableMaker = new DraggableMaker()
      draggableMaker.makeDraggable( newOne )
      newOne.shape.onMouseClicked = (event) =>
        addTo.children += new furnitureInfoPanel(newOne, addTo)
      listOfFurniture += newOne
      println(listOfFurniture)


  this.children = Array( furnitureName, f.shape, addButton)

/** Tekee huonekaluista liikuteltavia */
class DraggableMaker:

  private var mouseAnchorX = 0.0
  private var mouseAnchorY = 0.0
  private var mouseOffsetFromNodeX = 0.0
  private var mouseOffsetFromNodeY = 0.0

  def makeDraggable( a: Furniture ) =

    var n = a.shape
    n.setOnMousePressed((event) =>
      mouseAnchorX = event.getSceneX
      mouseAnchorY = event.getSceneY
      mouseOffsetFromNodeX = a.x - mouseAnchorX
      mouseOffsetFromNodeY = a.y - mouseAnchorY)

    n.setOnMouseDragged((event) =>
      n.setTranslateX(event.getSceneX - mouseAnchorX )
      n.setTranslateY(event.getSceneY - mouseAnchorY ) )

    n.setOnMouseReleased((event) =>
      n.setLayoutX( event.getSceneX + mouseOffsetFromNodeX)
      n.setLayoutY( event.getSceneY + mouseOffsetFromNodeY)
      a.x = event.getSceneX + mouseOffsetFromNodeX
      a.y = event.getSceneY + mouseOffsetFromNodeY
      n.setTranslateX(0)
      n.setTranslateY(0))

/** Ikkuna jonka avulla voidaan vaihtaa kuvion väriä */
class furnitureInfoPanel(n: Furniture, addTo: Pane) extends VBox:

  this.prefHeight = 100
  this.prefWidth = 200
  background = Background.fill(White)

  val label1 = new Label("Modify this piece of furniture")
  val label2 = new Label("Change the color")
  val colorPicker = ColorPicker( Blue )

  val submitButton = new Button( "Submit changes"):
    onAction = (event) =>
      n.changeColor( colorPicker.getValue )
      addTo.children.remove(this)


  this.children = Array( label1, colorPicker, submitButton )


