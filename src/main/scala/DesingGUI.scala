import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ScrollPane, Spinner}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{Background, Border, BorderImage, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, HBox, Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Ellipse, Rectangle}
import scalafx.scene.paint.Color.*
import scalafx.stage.{FileChooser, Stage}

import java.io.FileInputStream
import scala.collection.mutable.ListBuffer
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.image.Image.sfxImage2jfx
import scalafx.stage.FileChooser.{ExtensionFilter, sfxFileChooser2jfx}
import scalafx.Includes.jfxImage2sfx

import javax.imageio.ImageIO

/** Luokan alku, johon voi jo lisätä oman pohjapiirrustuksen, ei käsittele virheellisiä syötteitä **/

object DesingGUI extends JFXApp3:

  var floorPlanScale = 0.0
  var amountOfLamps = 0
  var image = Image(FileInputStream("src/test/piirrustus.jpeg"))

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 1000
      height = 600
      resizable = false

    /** Alkuskaalan kohdalla huoneen leveys on 9m */
    floorPlanScale = (stage.width.toDouble * 3/4) / 900.0
    /** Tilaa pohjapalkille */
    val spaceForBottom = stage.height.toDouble - 85

    /** Päänäkymän osia */
    val root = VBox()

    val scene1 = Scene(parent = root)

    stage.scene = scene1

    val sidePanel = new VBox()

    val scroll =  new ScrollPane()
    scroll.background = Background.fill(Black)
    scroll.prefViewportHeight = spaceForBottom
    scroll.prefViewportWidth = stage.width.toDouble / 4
    scroll.setContent( sidePanel )
    scroll.hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    scroll.hmax = 0

    val furniturePane = new Pane():
      prefHeight = spaceForBottom
      prefWidth  = stage.width.toDouble * 3/4
    furniturePane.setLayoutX(0)
    furniturePane.setLayoutY(0)

    /** Asettaa huonekaluikkunan taustan */
    var mainScene = new ImageView(Image(FileInputStream("src/test/piirrustus.jpeg"))):
      fitHeight = spaceForBottom
      fitWidth = stage.width.toDouble * 3/4
    mainScene.setLayoutX(0)
    mainScene.setLayoutY(0)

    var stack = new StackPane():
      children = Array( mainScene, furniturePane)

    val mainView = new HBox():
      children = Array(stack, scroll)


    /** Pohjapiirustuksen suunnittelunäkymän osia */
    val root2 = VBox()

    val sidePanel2 = new VBox()

    val scroll2 = new ScrollPane()
    scroll2.background = Background.fill(Black)
    scroll2.prefViewportHeight = spaceForBottom
    scroll2.prefViewportWidth = stage.width.toDouble / 4
    scroll2.setContent( sidePanel2 )
    scroll2.hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    scroll2.hmax = 0

    val floorplanDesignPane = new Pane():
      prefHeight = spaceForBottom
      prefWidth  = stage.width.toDouble * 3/4

    val backgroundRectangle = new Rectangle():
      height = spaceForBottom
      width = stage.width.toDouble * 3/4
      fill = White
    backgroundRectangle.setLayoutX(0)
    backgroundRectangle.setLayoutY(0)

    var stack2 = new StackPane():
      children = Array( backgroundRectangle, floorplanDesignPane )

    val secondView = new HBox():
      children = Array(stack2, scroll2)

    /** Näiden ListBuffereiden avulla pidetään kirjaa ohjelmassa olevista huonekaluista */
    val allFurniture = ListBuffer[Furniture]()
    val allWalls = ListBuffer[Furniture]()




    /** Tehdään näkymässä näykyvät huonekalut ja niiden huonekalupaneelit */
    val testFurniture = new Furniture("Sofa", 200, 100, true, Rectangle(200, 100), 300, 300, OrangeRed, false, Image( FileInputStream("src/main/Pictures/sohva.jpeg")))
    val testFurniture2 = new Furniture( "Table", 40, 40, true, Circle(40), 200, 200, Blue, false, Image( FileInputStream("src/main/Pictures/table.jpeg")))
    val testFurniture4 = new Furniture( "Coffee table", 100, 50, true, Ellipse(100, 100, 30, 20), 100, 100, Green, false, Image( FileInputStream("src/main/Pictures/coffeeTable.jpeg")))
    val testFurniture5 = new Rug( 100, 100, 100, 100, GreenYellow)
    val testLamp = new Lamp( 20, 100, 100, Yellow)

    val sofaPanel = new FurniturePanel( testFurniture, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val tablePanel = new FurniturePanel( testFurniture2, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val sofaTablePanel = new FurniturePanel( testFurniture4, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val rugPanel = new FurniturePanel( testFurniture5, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val lampPanel2 = new FurniturePanel(testLamp,(stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)

    val testWall = new Wall(200, 10, 300, 300, Black)
    val testDoor = new Door(100, 100, 300, 300, White)
    val testWindow = new Window(100, 10, 300, 300)
    val testCounter = new Counter( 100, 100, 300, 300, Black)

    val wallPanel = new FurniturePanel(testWall, (stage.width.toDouble / 4), (spaceForBottom / 3), floorplanDesignPane, allWalls)
    val doorPanel = new FurniturePanel(testDoor,(stage.width.toDouble / 4), (spaceForBottom / 3), floorplanDesignPane, allWalls)
    val windowPanel = new FurniturePanel( testWindow, (stage.width.toDouble / 4), (spaceForBottom / 3), floorplanDesignPane, allWalls)
    val counterPanel = new FurniturePanel(testCounter,(stage.width.toDouble / 4), (spaceForBottom / 3), floorplanDesignPane, allWalls)


    /** PÄÄNÄKYMÄN NAPIT */

    /** Nappia painamalla käyttäjä saa lisättyä oman pohjapiirustuksensa ohjelmaan.
     * Tiedoston on oltava kuvatiedosto. Käyttäjän oletetaan lisäävän ainoastaan
     * pohjapiirustuksia, ei suodata erilaisia kuvatiedostoja.
     * Kun lisätään uusi pohjapiirustus ohjelma poistaa aikaisemmat elementit */
    val addButton = new Button( "Add floorplan"):
      onAction = (event) =>
        val fileChooser = new FileChooser():
          extensionFilters.add( ExtensionFilter("jpg and png", Seq("*.jpg", "*.png")) )
        val floorplan = fileChooser.showOpenDialog(stage)
        if floorplan != null then
          val image = Image(FileInputStream(floorplan.getAbsolutePath))
          mainScene = new ImageView(image):
            fitHeight = stage.height.toDouble - 80
            fitWidth = stage.width.toDouble * 3/4
          allFurniture.clear()
          allWalls.clear()
          amountOfLamps = 0
          furniturePane.children.clear()
          stack.children.clear()
          stack.children = Array( mainScene, furniturePane)
          scaleInput(allFurniture).show()


    /** Kutsutaan metodia saveButtonMaker, tallennusnappien luomiseksi*/
    val save1 = saveButtonMaker(stage, mainScene, stack)
    val bottomBar = bottomBarMaker()

    val restartButton = new Button( "Restart" ):
      onAction = (event) =>
        furniturePane.children.clear()
        allFurniture.clear()
        amountOfLamps = 0

    val scaleButton1 = new Button("Change scale"):
      onAction = (event) =>
        scaleInput(allFurniture).show()


    val designYourOwnButton = new Button( "Design your own floorplan"):
      onAction = (event) =>
        scene1.root = root2

    /** POHJAPIIRUSTUKSEN SUUNNITTELUNÄKYMÄN NAPIT */
    
    val save2 = saveButtonMaker(stage, mainScene, stack2)

    val useThisFloorPlanButton = new Button( "Use this floorplan" ):
      onAction = (event) =>
        allFurniture.addAll( allWalls)
        val writable = new WritableImage(mainScene.getFitWidth.toInt, mainScene.getFitHeight.toInt)
        stack2.snapshot(null, writable)
        val image = sfxImage2jfx(writable)
        mainScene = new ImageView(image):
            fitHeight = spaceForBottom
            fitWidth = stage.width.toDouble * 3/4
        stack.children.clear()
        stack.children = Array(mainScene, furniturePane)
        scene1.root = root

    val bottomBar2 = bottomBarMaker()

    val restartButton2 = new Button( "Restart" ):
      onAction = (event) =>
        floorplanDesignPane.children.clear()
        allWalls.clear()

    val scaleButton2 = new Button("Change scale"):
      onAction = (event) =>
        scaleInput(allWalls).show()

    val returnButton = new Button( "Go back"):
      onAction = (event) =>
        scene1.root = root


    /** Asetetaan lapset */
    
    bottomBar.children = Array(addButton, save1, designYourOwnButton, restartButton, scaleButton1)
    
    sidePanel.children = Array(lampPanel2, tablePanel, sofaPanel, sofaTablePanel, rugPanel )
    
    root.children = Array(mainView, bottomBar)
    
    bottomBar2.children = Array(returnButton, save2, restartButton2, useThisFloorPlanButton, scaleButton2)
    
    sidePanel2.children = Array( wallPanel, doorPanel, windowPanel, counterPanel ) 
    
    root2.children = Array(secondView, bottomBar2)

  end start


  /** Metodi, joka tekee uuden nappulan, jolla voi tallentaa tietyn stackPanen kuvana */
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
  end saveButtonMaker

  /** Metodi, joka laatii alapalkin. Vähentää koodin toisteisuutta*/
  def bottomBarMaker(): HBox =
    new HBox():
      val stroke = BorderStroke(Black, BorderStrokeStyle.Solid, CornerRadii(1), BorderWidths(5))
        padding = Insets.apply(10, 10, 10, 10)
        spacing = 10
        background = Background.fill(Pink)
        border = Border( Array(stroke), Array[BorderImage]())
  end bottomBarMaker

  /** Metodi, joka mahdollistaa pohjapiirustuksen skaalan muuttamiseen, eli siis käyttäjä voi muuttaa pohjapiirustuksen
   * leveyttä ja korkeutta, jolloin skaala muuttuu myös*/
  def scaleInput(f: ListBuffer[Furniture]): Stage =

    val stage = new Stage()
    stage.setWidth(300)
    stage.setHeight(300)
    stage.setX(300)
    stage.setY(300)

    val pane = new VBox()

    val label1 = new Label("The width of your floorplan")

    val widthField = new Spinner[Double](0.1, 10, 2, 0.1)



    val submit = new Button( "Submit"):
      onAction = (event) =>
        floorPlanScale = (1000 * 3/4) / (widthField.getValue * 100)
        stage.close()

    pane.children = Array( label1, widthField, submit)

    val scene = new Scene(pane)
    stage.setScene(scene)
    stage

  end scaleInput

end DesingGUI
