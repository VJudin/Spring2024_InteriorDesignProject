import javafx.scene.Node
import scalafx.application.JFXApp3
import scalafx.event.EventHandler
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, ScrollPane, TextField}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.input.MouseDragEvent
import scalafx.scene.layout.{Background, ColumnConstraints, GridPane, HBox, Pane, RowConstraints, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Ellipse, Rectangle, Shape}
import scalafx.scene.paint.Color.*
import scalafx.stage.{FileChooser, Popup}
import scalafx.stage.StageIncludes.jfxFileChooser2sfx

import java.awt.Desktop
import java.awt.event.{MouseEvent, MouseListener, MouseMotionListener}
import java.io.FileInputStream
import scala.collection.mutable.ListBuffer
import scalafx.Includes.jfxImage2sfx
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.embed.swing.SwingFXUtils
import scalafx.stage.FileChooser.ExtensionFilter

import javax.imageio.ImageIO

/** Luokan alku, johon voi jo lisätä oman pohjapiirrustuksen, ei käsittele virheellisiä syötteitä **/

object DesingGUI extends JFXApp3:

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 1000
      height = 600
      resizable = false

    val root = VBox()


    val scene1 = Scene(parent = root)
    stage.scene = scene1

    val sidePanel = new VBox()
    val scroll =  new ScrollPane()
    scroll.background = Background.fill(White)
    scroll.prefViewportHeight = stage.height.toDouble - 70
    scroll.prefViewportWidth = stage.width.toDouble / 4
    scroll.setContent( sidePanel )

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

/** Testi huonekaluja ja huonekaluikkunoita */
    val testFurniture = new Furniture("Sofa", 60, 60, true, Rectangle(60, 60), 300, 300, Red, false)
    val testFurniture2 = new Furniture( "Table", 40, 40, true, Circle(40), 200, 200, Blue, false)
    val testFurniture3 = new Furniture( "Lamp", 20, 20, false, Circle(20), 100, 100, Yellow, true)
    val testFurniture4 = new Furniture( "Coffee table", 30, 20, true, Ellipse(100, 100, 30, 20), 100, 100, Green, false)
    val allFurniture = ListBuffer[Furniture]()
    val sofaPanel = new FurniturePanel( testFurniture, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), root2, allFurniture)
    val tablePanel = new FurniturePanel( testFurniture2, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), root2, allFurniture)
    val lampPanel = new FurniturePanel( testFurniture3, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), root2, allFurniture)
    val sofaTablePanel = new FurniturePanel( testFurniture4, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), root2, allFurniture)


    var mainScene = new ImageView(Image(FileInputStream("/Users/vilmajudin/Desktop/Koulu hommat/Vuosi 1/Periodi 3/MagicOfInteriorDesign/src/test/piirrustus.jpeg"))):
      fitHeight = stage.height.toDouble - 70
      fitWidth = stage.width.toDouble * 3/4
    var stack = new StackPane():
      children = Array( mainScene, root2)


    val mainView = new HBox():
      children = Array(stack, scroll)


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


    //val scene2 = Scene(parent = popUp)



    val addButton = new Button( "Add floorplan"):
      onAction = (event) =>
        val fileChooser = new FileChooser():
          extensionFilters.add( ExtensionFilter("jpg and png", Seq("*.jpg", "*.png")) )
        val floorplan = fileChooser.showOpenDialog(stage)
        if floorplan != null then
          mainScene = new ImageView(Image(FileInputStream(floorplan.getAbsolutePath))):
            fitHeight = stage.height.toDouble - 70
            fitWidth = stage.width.toDouble * 3/4
          stack.children.clear()
          stack.children = Array( mainScene, root2)


    val saveButton = new Button( "Save floorplan" ):
      onAction = (event) =>
        val fileChooser = new FileChooser():
          extensionFilters.add( ExtensionFilter("jpg and png", Seq("*.jpg", "*.png")) )
        fileChooser.setTitle("Save file")
        val fileToSave = fileChooser.showSaveDialog(stage)
        if fileToSave != null then
          var writable = new WritableImage( mainScene.getFitWidth.toInt, mainScene.getFitHeight.toInt)
          stack.snapshot(null, writable)
          var rendered = SwingFXUtils.fromFXImage(writable, null)
          ImageIO.write(rendered, "png", fileToSave)


    val bottomBar = new HBox():
      padding = Insets.apply(10, 10, 10, 10)
      spacing = 10
      background = Background.fill(White)


    bottomBar.children = Array(addButton, saveButton)
    sidePanel.children = Array(lampPanel, tablePanel, sofaPanel, sofaTablePanel )
    root.children = Array(mainView, bottomBar)


  end start

end DesingGUI

/** EI TOIMI AINAKAAN VIELÄ */
def wallAdder( where: Pane, from: Image, allFurniture: ListBuffer[Furniture] ) =
  val reader = from.getPixelReader
  val untilX = where.width.toInt
  val untilY = where.height.toInt
  for x <- 0 to(untilX, 1) do
    for y <- 0 to(untilY, 1) do
      if reader.getColor( x, y ) == Black then
        var newWall = Wall(1, 1, x, y, Black)
        where.children += newWall.shape
        newWall.shape.setLayoutX(x)
        newWall.shape.setLayoutY(y)
        allFurniture += newWall
