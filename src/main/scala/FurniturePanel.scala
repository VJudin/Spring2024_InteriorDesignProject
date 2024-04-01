import javafx.geometry
import javafx.scene.canvas
import javafx.scene.input.MouseButton
import scalafx.geometry.{Insets, Point2D}
import scalafx.scene.control.{Button, ColorPicker, Label}
import scalafx.scene.layout.{Background, GridPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color.{Blue, Green, White}
import scalafx.scene.shape.{Circle, Rectangle, Shape}
import scalafx.scene.text.Font
import scalafx.geometry
import scalafx.scene.{Node, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.MouseEvent.MouseMoved
import scalafx.scene.shape.Shape.{intersect, sfxShape2jfx}
import scalafx.scene.SceneIncludes.jfxColor2sfx
import scalafx.scene.SceneIncludes.jfxShape2sfx
import scalafx.stage.{Popup, Stage}

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
      addTo.children += shape
      val draggableMaker = new DraggableMaker()
      draggableMaker.makeDraggable( newOne, listOfFurniture )
      newOne.shape.onMouseClicked  = (event) =>
        if event.getButton == MouseButton.SECONDARY then
          popUpMaker(newOne).show()
      listOfFurniture += newOne
      println(listOfFurniture)


  this.children = Array( furnitureName, f.shape, addButton)


/** Tekee huonekaluista liikuteltavia */
class DraggableMaker:

  private var mouseAnchorX = 0.0
  private var mouseAnchorY = 0.0
  private var mouseOffsetFromNodeX = 0.0
  private var mouseOffsetFromNodeY = 0.0
  private var priorPositionX = 0.0
  private var priorPositionY = 0.0

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
    var c = b.map( x => x.shape)

    n.setOnMousePressed((event) =>
      mouseAnchorX = event.getSceneX
      mouseAnchorY = event.getSceneY
      mouseOffsetFromNodeX = a.x - mouseAnchorX
      mouseOffsetFromNodeY = a.y - mouseAnchorY
      priorPositionX = n.getLayoutX
      priorPositionY = n.getLayoutY)

    n.setOnMouseDragged((event) =>
      n.setTranslateX(event.getSceneX - mouseAnchorX )
      n.setTranslateY(event.getSceneY - mouseAnchorY ))

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

def popUpMaker(n: Furniture): Stage =
  val stage = new Stage()
  stage.setWidth(300)
  stage.setHeight(300)
  stage.setX( 300 )
  stage.setY( 300 )

  val pane = new VBox():
    margin = Insets(20)
    spacing = 20
    padding = Insets(20, 20, 20, 20)



  val label1 = new Label("Modify this piece of furniture")
  val label2 = new Label("Change the color")
  val colorPicker = ColorPicker( n.color )
  val shape = n.copy().shape
  shape.fill = n.color

  colorPicker.onAction = (event) => shape.fill = colorPicker.getValue

  val submitButton = new Button( "Submit changes"):
    onAction = (event) =>
      n.changeColor( colorPicker.getValue )
      stage.close()

  pane.children = Array( label1, shape, colorPicker, submitButton )

  val scene = new Scene(pane)

  stage.setScene(scene)

  stage