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