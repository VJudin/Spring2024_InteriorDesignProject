import DesingGUI.{amountOfLamps, floorPlanScaleX, floorPlanScaleY}
import javafx.scene.input.MouseButton
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{
  Background,
  Border,
  BorderImage,
  BorderStroke,
  BorderStrokeStyle,
  BorderWidths,
  CornerRadii,
  Pane,
  VBox
}
import scalafx.scene.paint.Color.Pink
import scalafx.scene.shape.Shape
import scalafx.scene.text.Font
import scalafx.geometry
import scalafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.scene.shape.Shape.sfxShape2jfx

import scala.collection.mutable.ListBuffer

/** Muuttuja joka mahdollistaa lamppujen määrän seuraamisen sekä sitä kautta
  * indeksien muuttamisen, selkenee katsottaessa indexToAddTo muuttujaa.
  * Varmistetaan siis että lamput ovat kaikkien muiden huonekalujen yläpuolella
  * käyttöliittymän näkymässä
  */

class FurniturePanel(
  f: Furniture,
  givenWidth: Double,
  givenHeight: Double,
  addTo: Pane,
  listOfFurniture: ListBuffer[Furniture]
) extends VBox:

  this.prefHeight = givenHeight
  this.prefWidth = givenWidth

  val furnitureName = new Label(f.fname):
    font = Font(14)

  /** Background colours, spacing and padding */
  background = Background.fill(Pink)
  val stroke = BorderStroke(
    Color.Black,
    BorderStrokeStyle.Solid,
    CornerRadii(0),
    BorderWidths(5)
  )
  border = Border(Array(stroke), Array[BorderImage]())
  spacing = 6
  padding = Insets(20, 20, 20, 20)

  /** Button that makes the furniture appear on the screen */
  var addButton = new Button(s"Add new ${f.fname}"):
    onAction = (event) => addFurniture()

  private val draggableMaker = new DraggableMaker()
  private val popUpMaker = new PopUpMaker

  /** Method for adding a piece of furniture */
  private def addFurniture(): Unit =
    /** A copy of the furniture from the panel is created, it's x and y
     * coordinates are changed so that it appears at the correct spot on the
     * screen
     */
    var newOne = f.copy()
    val shape = newOne.shape
    newOne.x = addTo.prefWidth.toDouble / 2
    newOne.y = addTo.prefHeight.toDouble / 2

    /** This variable checks which index the new furniture is added to. Indexing
      * the furniture allows for the rugs to be below all the other furniture
      * and for lamps to be on top of all the other furniture.
      */
    var indexToAddTo =
      f match
        case f: Rug =>
          0
        case f: Lamp =>
          amountOfLamps += 1
          addTo.children.length
        case f: Wall   => 0
        case f: Door   => 0
        case f: Window => 0
        case _         => addTo.children.length - amountOfLamps

    /** The new furniture is added to the list of all the furniture and the
      * Pane, which makes it appear on the GUI
      */
    listOfFurniture += newOne
    addTo.children.add(indexToAddTo, shape)

    /** Scale the furniture and add set the coordinates correctly
      */
    shape.setScaleX(DesingGUI.floorPlanScaleX)
    shape.setScaleY(DesingGUI.floorPlanScaleY)
    shape.setLayoutX(newOne.x)
    shape.setLayoutY(newOne.y)

    /** The furniture is made Draggable so the user can move it in the GUI */
    draggableMaker.makeDraggable(newOne, listOfFurniture, addTo)

    /** The customization window pops up right after the furniture is added */
    popUpMaker.makePopUp(newOne, addTo, listOfFurniture).show()

    /** When the furniture is clicked with the second mouse button, the
      * customization window pops up
      */
    newOne.shape.onMouseClicked = (event) =>
      if event.getButton == MouseButton.SECONDARY then
        popUpMaker.makePopUp(newOne, addTo, listOfFurniture).show()

  this.children = Array(furnitureName, f.shape, addButton)
