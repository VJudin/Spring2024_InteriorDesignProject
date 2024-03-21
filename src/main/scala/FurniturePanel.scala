import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Background, GridPane, HBox, VBox}
import scalafx.scene.paint.Color.White
import scalafx.scene.shape.{Circle, Rectangle, Shape}
import scalafx.scene.text.Font
import scalafx.geometry
import scalafx.scene.Node
import scalafx.scene.shape.Shape.sfxShape2jfx


class FurniturePanel (f: Furniture, givenWidth: Double, givenHeight: Double, addTo: GridPane ) extends VBox:


  val panel = this

  background = Background.fill(White)
  
  f.shape.fill = f.color
  val furnitureName = new Label(f.fname):
    font = Font(14)


  val movable = new Furniture(f.fname, f.width, f.lenght, false, f.shape, f.x, f.y, f.color, false)

  spacing = 6
  padding = Insets(30, 30, 30, 30)


  var addButton = new Button(s"Add new ${f.fname}"):
    onAction = (event) =>
      var newOne = f.copy()
      newOne.shape.fill = newOne.color
      addTo.add(newOne.shape, 0, 0)
      //panel.children += newOne.shape

  this.children = Array( furnitureName, f.shape, addButton)



