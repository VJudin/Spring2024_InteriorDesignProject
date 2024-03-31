import javafx.scene.Node
import scalafx.application.JFXApp3
import scalafx.event.EventHandler
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseDragEvent
import scalafx.scene.layout.{Background, ColumnConstraints, GridPane, HBox, Pane, RowConstraints, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle, Shape}
import scalafx.scene.paint.Color.*
import scalafx.stage.{FileChooser, Popup}

import java.awt.Desktop
import java.awt.event.{MouseEvent, MouseListener, MouseMotionListener}
import java.io.FileInputStream
import scala.collection.mutable.ListBuffer

/** Luokan alku, johon voi jo lisätä oman pohjapiirrustuksen, ei käsittele virheellisiä syötteitä **/

object DesingGUI extends JFXApp3:

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 1000
      height = 600

    val root = VBox()

    val scene1 = Scene(parent = root)
    stage.scene = scene1

    val sidePanel = new VBox()
    

    val rectangle2 = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble / 4
      height = (stage.height.toDouble-70) / 3
      fill = Red

    val rectangle3 = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble / 4
      height = (stage.height.toDouble-70) / 3
      fill = Green

    val root2 = new Pane():
      prefHeight = stage.height.toDouble - 70
      prefWidth  = stage.width.toDouble * 3/4


    val testFurniture = new Furniture("Sofa", 60, 60, false, Rectangle(60, 60), 0, 0, Red, false)
    val testFurniture2 = new Furniture( "Table", 30, 30, false, Circle(40), 0, 0, Blue, false)
    val allFurniture = ListBuffer[Furniture]()
    val sofaPanel = new FurniturePanel( testFurniture, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), root2, allFurniture)
    val tablePanel = new FurniturePanel( testFurniture2, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), root2, allFurniture)
    

    var mainScene = new ImageView(Image(FileInputStream("/Users/vilmajudin/Desktop/Koulu hommat/Vuosi 1/Periodi 3/MagicOfInteriorDesign/src/test/piirrustus.jpeg"))):
      fitHeight = stage.height.toDouble - 70
      fitWidth = stage.width.toDouble * 3/4


    var stack = new StackPane():
      children = Array( mainScene, root2)


    val mainView = new HBox():
      children = Array(stack, sidePanel)



/**    Saving code of popUp for possible later use
 *
 * val popUp = new VBox():
      val label = Label("Add the path to your floorplan")
      val text1 = TextField()
      val button = new Button("Submit"):
        onAction = (event) =>
          mainScene = new ImageView(Image(FileInputStream(text1.text.value))):
                  fitHeight = stage.height.toDouble - 70
                  fitWidth = stage.width.toDouble * 3/4
          stage.scene = scene1
          mainView.children.clear()
          mainView.children = Array( mainScene, sidePanel)
          text1.clear()
      text1.promptText = "Add the path to your floorplan here"
      children = Array( label, text1, button ) */

    val fileChooser = new FileChooser():
      title = "Open image of floorplan"

    //val scene2 = Scene(parent = popUp)

    val addButton = new Button( "Add floorplan"):
      onAction = (event) =>
        val floorplan = fileChooser.showOpenDialog(stage)
        if floorplan != null then
          mainScene = new ImageView(Image(FileInputStream(floorplan.getAbsolutePath))):
            fitHeight = stage.height.toDouble - 70
            fitWidth = stage.width.toDouble * 3/4
          stack.children.clear()
          stack.children = Array( mainScene, root2)

    /** Huonekalujen liikuttamiseen liittyvää koodia*/
    var mouseX = 0.0
    var mouseY = 0.0
    var f = new Rectangle():
      width = 30
      height = 60



    val bottomBar = new HBox():
      padding = Insets.apply(10, 10, 10, 10)
      spacing = 10
      background = Background.fill(White)
      children = Array(addButton)
    val n = new furnitureInfoPanel(testFurniture, root2)
    sidePanel.children = Array(rectangle2, tablePanel, sofaPanel)
    root.children = Array(mainView, bottomBar)


  end start

end DesingGUI

