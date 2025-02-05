import DesingGUI.amountOfLamps
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ColorPicker, Label, Spinner}
import scalafx.scene.layout.{GridPane, HBox, Pane, VBox}
import scalafx.scene.shape.{Arc, Circle, Rectangle}
import scalafx.scene.transform.Rotate
import scalafx.stage.Stage
import scalafx.scene.SceneIncludes.jfxColor2sfx
import scalafx.scene.image.ImageView

import scala.collection.mutable.ListBuffer

class PopUpMaker:

  /** Creates a customization window for a piece of furniture */
  def makePopUp(
    n: Furniture,
    furnitureIsIn: Pane,
    listOfFurniture: ListBuffer[Furniture]
  ): Stage =

    val stage = new Stage()
    stage.setX(200)
    stage.setY(200)

    var w = 0.0
    var h = 0.0

    /** Checks if the width or height is bigger, used later regarding the layout
      * of the popup
      */
    val bigger =
      if n.width < n.lenght then n.lenght
      else n.width

    /** A new GridPane to which all the elements of the popup are added */
    val grid = new GridPane():
      hgap = 10
      vgap = 10
      padding = Insets(10, 10, 10, 10)

    /** Panel that has all the changeable attributes of a piece of furniture */
    val changePanel = new VBox():
      spacing = 20

    /** Panel that has the submit and delete buttons */
    val submitOrDelete = new HBox():
      spacing = 20
      padding = Insets(20, 20, 20, 20)

    /** Panel that has a copy of the shape of a piece of furniture */
    val shapePanel = new VBox():
      val a = bigger / 2
      padding = Insets(a, a, a, a)

    val label1 = new Label("Modify this piece of furniture")

    /** A copy of the piece of furniture having all the same attributes as the
      * original
      */
    val copyOfFurniture = n.copy()

    copyOfFurniture.changeColor(n.color)

    val shape = copyOfFurniture.shape

    shape.getTransforms.addAll(n.shape.getTransforms)

    /** Initialize colorpicker, the color of the model furniture changes as the
      * value of the colorpicker changes
      */
    val colorPicker = ColorPicker(n.color)

    colorPicker.onAction = (event) =>
      copyOfFurniture.changeColor(colorPicker.getValue)

    /** Create spinners and their labels and set them to be next to oneanother
      */
    val scaleWidth = new Spinner[Double](10.0, 500, n.width, 10.0)

    val scaleHeight = new Spinner[Double](10.0, 500, n.lenght, 10.0)

    val label2 = new Label("cm")

    val label3 = new Label("cm")

    val scalingWidth = new HBox():
      spacing = 5
      children = Array(scaleWidth, label2)

    val scalingHeight = new HBox():
      spacing = 5
      children = Array(scaleHeight, label3)

    /** Button that makes the changes appear on the GUI */
    val submitButton = new Button("Submit changes"):

      onAction = (event) =>
        n.changeColor(colorPicker.getValue)
        n.shape.getTransforms.clear()
        n.shape.getTransforms.addAll(copyOfFurniture.shape.getTransforms)
        w = scaleWidth.getValue
        h = scaleHeight.getValue
        n.changeSize(scaleWidth.getValue, scaleHeight.getValue)
        stage.close()

    /** Button that allows for the deletion of a piece of furniture */
    val deleteButton = new Button("Delete this furniture"):
      onAction = (event) =>
        var indexOf = listOfFurniture.indexOf(n)
        listOfFurniture.remove(indexOf)
        furnitureIsIn.children.remove(n.shape)
        if n.fname == "Lamp" then amountOfLamps -= 1
        stage.close()

    /** Button that allows the user to rotate the furniture. For some furniture
      * the center of that piece of furniture has to be calculated separately so
      * that the furniture rotates from the middle
      */
    val rotateButton = new Button("Rotate this furniture"):
      onAction = (event) =>
        n.shape match
          case shape: Rectangle =>
            copyOfFurniture.shape.getTransforms.add(
              new Rotate(45, n.width / 2, n.lenght / 2)
            )
          case shape: Arc =>
            copyOfFurniture.shape.getTransforms.add(
              new Rotate(45, n.width / 2, -n.width / 2)
            )
          case _ =>
            copyOfFurniture.shape.getTransforms.add(new Rotate(45, 0, 0))

    /** The changable attributes change depending on the piece of furniture and
      * as such this match case structure makes sure that only the changable
      * attributes appear on the popup
      */
    n.shape match
      case shape: Circle =>
        changePanel.children = Array(colorPicker, scalingWidth)
      case shape: Arc =>
        changePanel.children = Array(rotateButton, scalingWidth, scalingHeight)
      case _ =>
        if n.fname == "Counter" then
          changePanel.children =
            Array(rotateButton, scalingWidth, scalingHeight)
        else if n.fname == "Wall" then
          changePanel.children =
            Array(rotateButton, scalingWidth, scalingHeight)
        else if n.fname == "Window" then
          changePanel.children =
            Array(rotateButton, scalingWidth, scalingHeight)
        else
          changePanel.children =
            Array(colorPicker, rotateButton, scalingWidth, scalingHeight)

    /** All furniture have a sample picture, it's made to an image view to allow
      * for easy scaling
      */
    val image = new ImageView(n.image)
    image.fitWidth = 300
    image.fitHeight = 300

    /** Adding the children to the panes and putting it together */
    shapePanel.children = Array(shape)
    submitOrDelete.children = Array(submitButton, deleteButton)
    grid.add(label1, 2, 0, 2, 1)
    grid.add(shape, 3, 2)
    grid.add(submitOrDelete, 2, 5, 3, 1)
    grid.add(changePanel, 4, 2, 3, 1)
    grid.add(image, 0, 0, 2, 7)

    /** If the user closes the window without submitting the changes, the
      * changes are not added
      */
    stage.onCloseRequest = (event) =>
      var indexOf = listOfFurniture.indexOf(n)
      listOfFurniture.remove(indexOf)
      furnitureIsIn.children.remove(n.shape)
      if n.fname == "Lamp" then amountOfLamps -= 1

    val scene = new Scene(grid)

    stage.setScene(scene)

    stage

end PopUpMaker
