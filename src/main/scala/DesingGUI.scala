import javafx.scene.Node
import scalafx.application.JFXApp3
import scalafx.event.EventHandler
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, ScrollPane, Spinner, TextField}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.input.{MouseButton, MouseDragEvent}
import scalafx.scene.layout.{Background, ColumnConstraints, GridPane, HBox, Pane, RowConstraints, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Ellipse, Rectangle, Shape}
import scalafx.scene.paint.Color.*
import scalafx.stage.{FileChooser, Stage}
import java.io.FileInputStream
import scala.collection.mutable.ListBuffer
import scalafx.embed.swing.SwingFXUtils
import scalafx.stage.FileChooser.{ExtensionFilter, sfxFileChooser2jfx}

import javax.imageio.ImageIO

/** Luokan alku, johon voi jo lisätä oman pohjapiirrustuksen, ei käsittele virheellisiä syötteitä **/

object DesingGUI extends JFXApp3:

  var floorPlanScale = 0.0

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 1000
      height = 600
      resizable = false

    val root = VBox()

    val root2 = VBox()


    val scene1 = Scene(parent = root)
    stage.scene = scene1

    val sidePanel = new VBox()
    val sidePanel2 = new VBox()

    val scroll =  new ScrollPane()
    scroll.background = Background.fill(White)
    scroll.prefViewportHeight = stage.height.toDouble - 70
    scroll.prefViewportWidth = stage.width.toDouble / 4
    scroll.setContent( sidePanel )
    scroll.hbarPolicy = ScrollPane.ScrollBarPolicy.Never

    val scroll2 = new ScrollPane()
    scroll2.background = Background.fill(White)
    scroll2.prefViewportHeight = stage.height.toDouble - 70
    scroll2.prefViewportWidth = stage.width.toDouble / 4
    scroll2.setContent( sidePanel2 )
    scroll2.hbarPolicy = ScrollPane.ScrollBarPolicy.Never

    val furniturePane = new Pane():
      prefHeight = stage.height.toDouble - 70
      prefWidth  = stage.width.toDouble * 3/4
    furniturePane.setLayoutX(0)
    furniturePane.setLayoutY(0)

    val floorplanDesignPane = new Pane():
      prefHeight = stage.height.toDouble - 70
      prefWidth  = stage.width.toDouble * 3/4

    val canvas = new Rectangle():
      height = stage.height.toDouble - 70
      width = stage.width.toDouble * 3/4
      fill = White
    canvas.setLayoutX(0)
    canvas.setLayoutY(0)



    floorPlanScale = (stage.width.toDouble * 3/4) / 900.0
    var planningScaleX = (stage.width.toDouble * 3 / 4) / 400
    var planningScaleY = (stage.height.toDouble - 70) / 200


/** Testi huonekaluja ja huonekaluikkunoita */

    var mainScene = new ImageView(Image(FileInputStream("/Users/vilmajudin/Desktop/Koulu hommat/Vuosi 1/Periodi 3/MagicOfInteriorDesign/src/test/piirrustus.jpeg"))):
      fitHeight = stage.height.toDouble - 70
      fitWidth = stage.width.toDouble * 3/4
    mainScene.setLayoutX(0)
    mainScene.setLayoutY(0)

    var stack = new StackPane():
      children = Array( mainScene, furniturePane)

    var stack2 = new StackPane():
      children = Array( canvas, floorplanDesignPane )


    val mainView = new HBox():
      children = Array(stack, scroll)

    val secondView = new HBox():
      children = Array(stack2, scroll2)

    val allFurniture = ListBuffer[Furniture]()
    def scaleInput(): Stage =

      val stage = new Stage()
      stage.setWidth(300)
      stage.setHeight(300)
      stage.setX(300)
      stage.setY(300)

      val pane = new VBox()

      val label1 = new Label("The width of your floorplan")
      val label2 = new Label("The height of your floorplan")

      val widthField = new Spinner[Double](0.1, 10, 2, 0.1)
      val heightField = new Spinner[Double](0.1, 10, 2, 0.1)

      val submit = new Button( "Submit"):
        onAction = (event) =>
          floorPlanScale = 750 / (widthField.getValue * 100)
          stage.close()

      pane.children = Array( label1, widthField, label2, heightField, submit)

      val scene = new Scene(pane)

      stage.setScene(scene)

      stage

    val addButton = new Button( "Add floorplan"):
      onAction = (event) =>
        val fileChooser = new FileChooser():
          extensionFilters.add( ExtensionFilter("jpg and png", Seq("*.jpg", "*.png")) )
        val floorplan = fileChooser.showOpenDialog(stage)
        if floorplan != null then
          val image = Image(FileInputStream(floorplan.getAbsolutePath))
          mainScene = new ImageView(image):
            fitHeight = stage.height.toDouble - 70
            fitWidth = stage.width.toDouble * 3/4
          allFurniture.clear()
          furniturePane.children.clear()
          stack.children.clear()
          stack.children = Array( mainScene, furniturePane)
          scaleInput().show()
          //popUpMaker(testFurniture, furniturePane, allFurniture).show()


    val testFurniture = new Furniture("Sofa", 200, 100, true, Rectangle(200, 100), 300, 300, Pink, false)
    val testFurniture2 = new Furniture( "Table", 40, 40, true, Circle(40), 200, 200, Blue, false)
    val testFurniture3 = new Furniture( "Lamp", 20, 20, false, Circle(20), 100, 100, Yellow, true)
    val testFurniture4 = new Furniture( "Coffee table", 100, 50, true, Ellipse(100, 100, 30, 20), 100, 100, Green, false)
    val sofaPanel = new FurniturePanel( testFurniture, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), furniturePane, allFurniture)
    val tablePanel = new FurniturePanel( testFurniture2, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), furniturePane, allFurniture)
    val lampPanel = new FurniturePanel( testFurniture3, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), furniturePane, allFurniture)
    val sofaTablePanel = new FurniturePanel( testFurniture4, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), furniturePane, allFurniture)

    val testWall = new Wall(10, 200, 300, 300, Black)
    val testDoor = new Door(100, 100, 300, 300, Black)
    val testWindow = new Window(100, 300, 300)
    val allWalls = ListBuffer[Furniture]()
    val wallPanel = new FurniturePanel(testWall, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), floorplanDesignPane, allWalls)
    val doorPanel = new FurniturePanel(testDoor,(stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), floorplanDesignPane, allWalls)
    val windowPanel = new FurniturePanel( testWindow, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), floorplanDesignPane, allWalls)

    val save1 = saveButtonMaker(stage, mainScene, stack)
    val save2 = saveButtonMaker(stage, mainScene, stack2)

    val bottomBar = new HBox():
      padding = Insets.apply(10, 10, 10, 10)
      spacing = 10
      background = Background.fill(White)

    val bottomBar2 = new HBox():
      padding = Insets.apply(10, 10, 10, 10)
      spacing = 10
      background = Background.fill(White)

    val designYourOwnButton = new Button( "Design your own floorplan"):
      onAction = (event) =>
        scene1.root = root2
        val returnButton = new Button( "Go back"):
          onAction = (event) =>
            scene1.root = root
        bottomBar2.children = Array(returnButton, save2)
        root2.children = Array(secondView, bottomBar2)

    val restartButton = new Button( "Restart" ):
      onAction = (event) =>
        furniturePane.children.clear()
        allFurniture.clear()

    bottomBar.children = Array(addButton, save1, designYourOwnButton, restartButton)
    sidePanel.children = Array(lampPanel, tablePanel, sofaPanel, sofaTablePanel )
    sidePanel2.children = Array( wallPanel, doorPanel, windowPanel )
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

def saveButtonMaker(s: Stage, mainView: ImageView, stack: StackPane): Button = new Button( "Save"):
  onAction = (event) =>
    val fileChooser = new FileChooser():
      extensionFilters.add( ExtensionFilter("PNG", "*.png") )
      extensionFilters.add( ExtensionFilter("JPEG", "*.jpg"))
    fileChooser.setTitle("Save file")
    val fileToSave = fileChooser.showSaveDialog(s)
    if fileToSave != null then
      var writable = new WritableImage( mainView.getFitWidth.toInt, mainView.getFitHeight.toInt)
      stack.snapshot(null, writable)
      var rendered = SwingFXUtils.fromFXImage(writable, null)
      ImageIO.write(rendered, "png", fileToSave)



