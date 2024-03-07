import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Background, HBox, VBox}
import scalafx.scene.paint.Color.White
import scalafx.scene.text.Font


class FurniturePanel (f: Furniture, givenWidth: Double, givenHeight: Double) extends VBox:

  background = Background.fill(White)

  val furnitureName = new Label(f.fname):
    font = Font(14)

  val addButton = new Button(s"Add new ${f.fname}")

  
  this.children = Array( furnitureName, addButton)



