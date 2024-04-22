import DesingGUI.floorPlanScale
import javafx.scene.input.MouseButton
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Background, Border, BorderImage, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, Pane, VBox}
import scalafx.scene.paint.Color.{Pink, White}
import scalafx.scene.shape.Shape
import scalafx.scene.text.Font
import scalafx.geometry
import scalafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.text.FontWeight.Black

import scala.collection.mutable.ListBuffer

var amountOfLamps = 0

class FurniturePanel (f: Furniture, givenWidth: Double, givenHeight: Double, addTo: Pane, listOfFurniture: ListBuffer[Furniture] ) extends VBox:


  this.prefHeight = givenHeight
  this.prefWidth = givenWidth

  val panel = this

  background = Background.fill(Pink)
  val stroke = BorderStroke(Color.Black, BorderStrokeStyle.Solid, CornerRadii(1), BorderWidths(5))

  val furnitureName = new Label(f.fname):
    font = Font(14)

  spacing = 6
  padding = Insets(20, 20, 20, 20)



  var addButton = new Button(s"Add new ${f.fname}"):
    onAction = (event) =>
      var newOne = f.copy()
      val shape = newOne.shape
      newOne.x = addTo.prefWidth.toDouble / 2
      newOne.y = addTo.prefHeight.toDouble / 2
      var indexToAddTo =
        f match
          case f: Rug =>
            0
          case f: Lamp =>
            amountOfLamps += 1
            listOfFurniture.length
          case _ => listOfFurniture.length - amountOfLamps
      listOfFurniture += newOne
      println(indexToAddTo)
      println(amountOfLamps)
      addTo.children.add( indexToAddTo, shape )
      shape.setScaleX( DesingGUI.floorPlanScale )
      shape.setScaleY(DesingGUI.floorPlanScale)
      shape.setLayoutX( newOne.x )
      shape.setLayoutY( newOne.y )
      val draggableMaker = new DraggableMaker()
      draggableMaker.makeDraggable( newOne, listOfFurniture, addTo )
      newOne.shape.onMouseClicked  = (event) =>
        if event.getButton == MouseButton.SECONDARY then
          popUpMaker(newOne, addTo, listOfFurniture).show()

  border = Border( Array(stroke), Array[BorderImage]())
  this.children = Array( furnitureName, f.shape, addButton )