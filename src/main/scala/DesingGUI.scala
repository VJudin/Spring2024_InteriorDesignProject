import testFurniture.{
  testBed,
  testBookshelf,
  testChair,
  testCoffeeTable,
  testCounter,
  testDoor,
  testLamp,
  testRoundTable,
  testRug,
  testRug2,
  testSofa,
  testTable,
  testWall,
  testWindow
}
import extraMethods.{bottomBarMaker, scaleInput}
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ScrollPane}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{Background, HBox, Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color.*
import scalafx.stage.FileChooser
import java.io.FileInputStream
import scala.collection.mutable.ListBuffer
import scalafx.scene.image.Image.sfxImage2jfx
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.Includes.jfxImage2sfx

object DesingGUI extends JFXApp3:

  var floorPlanScaleX = 0.0
  var floorPlanScaleY = 0.0
  var amountOfLamps = 0

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 1000
      height = 600
      resizable = false

    /** Space for the bottom bar */
    val spaceForBottom = stage.height.toDouble - 100

    /** Starting scale is 9m x 6m */
    floorPlanScaleX = (stage.width.toDouble * 3 / 4) / 900
    floorPlanScaleY = spaceForBottom / 600

    /** Parts for the main view */
    val root = VBox()

    val scene1 = Scene(parent = root)

    stage.scene = scene1

    val sidePanel = new VBox()

    /** Create a scrollPane which will allow for room for more furniture in the
      * side panel
      */
    val scroll = new ScrollPane()
    scroll.background = Background.fill(Black)
    scroll.prefViewportHeight = spaceForBottom
    scroll.prefViewportWidth = stage.width.toDouble / 4
    scroll.setContent(sidePanel)
    scroll.hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    scroll.hmax = 0
    scroll.prefViewportHeight = stage.height.toDouble

    val furniturePane = new Pane():
      prefHeight = spaceForBottom
      prefWidth = stage.width.toDouble * 3 / 4
    furniturePane.setLayoutX(0)
    furniturePane.setLayoutY(0)

    /** Sets the background of the main view */
    var mainScene = new ImageView(
      Image(FileInputStream("src/test/piirrustus.jpeg"))
    ):
      fitHeight = spaceForBottom
      fitWidth = stage.width.toDouble * 3 / 4
    mainScene.setLayoutX(0)
    mainScene.setLayoutY(0)

    var stack = new StackPane():
      children = Array(mainScene, furniturePane)

    val mainView = new HBox():
      children = Array(stack, scroll)

    /** Parts of the main GUI */
    val root2 = VBox()

    val sidePanel2 = new VBox()

    val scroll2 = new ScrollPane()
    scroll2.background = Background.fill(Black)
    scroll2.prefViewportHeight = spaceForBottom
    scroll2.prefViewportWidth = stage.width.toDouble / 4
    scroll2.setContent(sidePanel2)
    scroll2.hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    scroll2.hmax = 0

    val floorplanDesignPane = new Pane():
      prefHeight = spaceForBottom
      prefWidth = stage.width.toDouble * 3 / 4

    val backgroundRectangle = new Rectangle():
      height = spaceForBottom
      width = stage.width.toDouble * 3 / 4
      fill = White
    backgroundRectangle.setLayoutX(0)
    backgroundRectangle.setLayoutY(0)

    var stack2 = new StackPane():
      children = Array(backgroundRectangle, floorplanDesignPane)

    val secondView = new HBox():
      children = Array(stack2, scroll2)

    /** These ListBuffers keep track of the furniture in the GUI
      */
    val allFurniture = ListBuffer[Furniture]()
    val allWalls = ListBuffer[Furniture]()
    val wallsInFloorPlan = ListBuffer[Furniture]()

    /** Create the furniture panels using the furniture panel method and the
      * test furniture created in SampleFurniture file.
      */

    val sofaPanel = new FurniturePanel(
      testSofa,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val tablePanel = new FurniturePanel(
      testRoundTable,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val sofaTablePanel = new FurniturePanel(
      testCoffeeTable,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val rugPanel = new FurniturePanel(
      testRug,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val lampPanel2 = new FurniturePanel(
      testLamp,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val roundRugPanel = new FurniturePanel(
      testRug2,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val bedPanel = new FurniturePanel(
      testBed,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val diningTablePanel = new FurniturePanel(
      testTable,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val chairPanel = new FurniturePanel(
      testChair,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val bookshelfPanel = new FurniturePanel(
      testBookshelf,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      furniturePane,
      allFurniture
    )

    val wallPanel = new FurniturePanel(
      testWall,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      floorplanDesignPane,
      allWalls
    )

    val doorPanel = new FurniturePanel(
      testDoor,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      floorplanDesignPane,
      allWalls
    )

    val windowPanel = new FurniturePanel(
      testWindow,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      floorplanDesignPane,
      allWalls
    )

    val counterPanel = new FurniturePanel(
      testCounter,
      (stage.width.toDouble / 4),
      (spaceForBottom / 3),
      floorplanDesignPane,
      allWalls
    )

    /** THE BUTTONS OF THE MAIN VIEW */

    /** By pressing the button the user can add their own floorplan to the
      * program. The floorplan has to be an image. The user is expected to add
      * only floorplans, the program does not verify what type of image the
      * image is. When a new floorplan is added, the elements on the floorplan
      * are deleted.
      */
    val addButton = new Button("Add floorplan"):
      onAction = (event) =>
        val fileChooser = new FileChooser():
          extensionFilters.add(
            ExtensionFilter("jpg and png", Seq("*.jpg", "*.png"))
          )
        val floorplan = fileChooser.showOpenDialog(stage)
        if floorplan != null then
          val image = Image(FileInputStream(floorplan.getAbsolutePath))
          mainScene = new ImageView(image):
            fitHeight = stage.height.toDouble - 80
            fitWidth = stage.width.toDouble * 3 / 4
          allFurniture.clear()
          allWalls.clear()
          wallsInFloorPlan.clear()
          amountOfLamps = 0
          furniturePane.children.clear()
          stack.children.clear()
          stack.children = Array(mainScene, furniturePane)
          scaleInput(allFurniture, stack, stage).show()

    /** Create a new buttonMaker object */
    val buttonmaker = new buttonMaker()

    /** Call the saveButtonMaker method from the ButtonMaker class */
    val save1 = buttonmaker.saveButtonMaker(stage, stack)

    /** Call the bottomBarMaker method to make the bottomBar */
    val bottomBar = bottomBarMaker()

    /** Button that clears the current view of all furniture. */
    val restartButton = new Button("Restart"):
      onAction = (event) =>
        furniturePane.children.clear()
        allFurniture.clear()
        allFurniture.addAll(wallsInFloorPlan)
        amountOfLamps = 0

    /** Button that allows the user to scale the floorplan */
    val scaleButton1 = new Button("Change scale"):
      onAction = (event) => scaleInput(allFurniture, stack, stage).show()

    /** Button that changes the view to allow the user to make their own
      * floorplan
      */
    val designYourOwnButton = new Button("Design your own floorplan"):
      onAction = (event) => scene1.root = root2

    /** FLOORPLAN DESIGN VIEW BUTTONS */

    /** Call the saveButtonMaker function to make a save button for the
      * floorplan design view
      */
    val save2 = buttonmaker.saveButtonMaker(stage, stack2)

    /** When the user presses this button, they will use the current floorplan
      * they have made, this means the view changes back to the main design
      * view, but this time the floorplan shown is one the user made..
      */
    val useThisFloorPlanButton = new Button("Use this floorplan"):
      onAction = (event) =>
        wallsInFloorPlan.clear()
        allFurniture.clear()
        allFurniture.addAll(allWalls)
        wallsInFloorPlan.addAll(allWalls)
        val writable = new WritableImage(
          mainScene.getFitWidth.toInt,
          mainScene.getFitHeight.toInt
        )
        stack2.snapshot(null, writable)
        val image = sfxImage2jfx(writable)
        mainScene = new ImageView(image):
          fitHeight = spaceForBottom
          fitWidth = stage.width.toDouble * 3 / 4
        allWalls.clear()
        floorplanDesignPane.children.clear()
        stack.children.clear()
        stack.children = Array(mainScene, furniturePane)
        scene1.root = root

    val bottomBar2 = bottomBarMaker()

    /** Clears the floorplan design view of all the elements */
    val restartButton2 = new Button("Restart"):
      onAction = (event) =>
        floorplanDesignPane.children.clear()
        allWalls.clear()

    val scaleButton2 = new Button("Change scale"):
      onAction = (event) => scaleInput(allWalls, stack2, stage).show()

    /** The user can return back to the design view to continue on their earlier
      * design
      */
    val returnButton = new Button("Go back"):
      onAction = (event) => scene1.root = root

    /** Set the children of the view */

    bottomBar.children =
      Array(addButton, save1, designYourOwnButton, restartButton, scaleButton1)

    sidePanel.children = Array(
      lampPanel2,
      tablePanel,
      diningTablePanel,
      chairPanel,
      sofaPanel,
      sofaTablePanel,
      bookshelfPanel,
      bedPanel,
      rugPanel,
      roundRugPanel
    )

    root.children = Array(mainView, bottomBar)

    bottomBar2.children = Array(
      returnButton,
      save2,
      restartButton2,
      useThisFloorPlanButton,
      scaleButton2
    )

    sidePanel2.children = Array(wallPanel, doorPanel, windowPanel, counterPanel)

    root2.children = Array(secondView, bottomBar2)

  end start

end DesingGUI
