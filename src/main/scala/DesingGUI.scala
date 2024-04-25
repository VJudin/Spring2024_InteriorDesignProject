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

  var floorPlanScaleX = 0.0
  var floorPlanScaleY = 0.0
  var amountOfLamps = 0

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 1000
      height = 600
      resizable = false


    /** Tilaa pohjapalkille */
    val spaceForBottom = stage.height.toDouble - 100

    /** Alkuskaalan kohdalla huoneen leveys on 9m ja korkeus 6 m */
    floorPlanScaleX = (stage.width.toDouble * 3/4) / 900
    floorPlanScaleY = spaceForBottom / 600

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
    scroll.prefViewportHeight = stage.height.toDouble

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
    val wallsInFloorPlan = ListBuffer[Furniture]()




    /** Tehdään näkymässä näykyvät huonekalut ja niiden huonekalupaneelit */
    val testFurniture = new Furniture("Sofa", 200, 100, true, Rectangle(200, 100), 300, 300, DarkSeaGreen, false, Image( FileInputStream("src/main/Pictures/sohva.jpeg")))
    val testFurniture2 = new Furniture( "Round Table", 150, 150, true, Circle(50), 200, 200, Blue, false, Image( FileInputStream("src/main/Pictures/table.jpeg")))
    val testFurniture4 = new Furniture( "Coffee table", 100, 50, true, Ellipse(100, 100, 30, 20), 100, 100, ForestGreen, false, Image( FileInputStream("src/main/Pictures/coffeeTable.jpeg")))
    val testFurniture5 = new Rug( 100, 100, 100, 100, Rectangle(100, 100), LightCyan, Image( FileInputStream("src/main/Pictures/Rug.jpeg")))
    val testFurniture6 = new Rug( 50, 50, 100, 100, Circle(50), LightCoral, Image(FileInputStream("src/main/Pictures/roundRug.jpeg")))
    val testFurniture7 = new Furniture( "Bed", 200, 180, true, Rectangle(200, 180),  300, 300, Beige, false, Image( FileInputStream("src/main/Pictures/bed.jpeg")))
    val testFurniture8 = new Furniture( "Table", 200, 150, true, Rectangle(200, 150), 300, 300, Brown, false, Image( FileInputStream("src/main/Pictures/diningTable.jpeg")) )
    val testFurniture9 = new Furniture( "Chair", 50, 50, true, Rectangle(50, 50), 300, 300, Black, false, Image( FileInputStream("src/main/Pictures/chair.jpeg")))
    val testFurniture10 = new Furniture( "Bookshelf", 200, 50, true, Rectangle(200, 50), 300, 300, RosyBrown, false, Image( FileInputStream("src/main/Pictures/bookshelf.jpeg")))
    val testLamp = new Lamp( 20, 100, 100, Yellow)

    val sofaPanel = new FurniturePanel( testFurniture, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val tablePanel = new FurniturePanel( testFurniture2, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val sofaTablePanel = new FurniturePanel( testFurniture4, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val rugPanel = new FurniturePanel( testFurniture5, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val lampPanel2 = new FurniturePanel(testLamp,(stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val roundRugPanel = new FurniturePanel(testFurniture6, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val bedPanel = new FurniturePanel( testFurniture7, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val diningTablePanel = new FurniturePanel( testFurniture8, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val chairPanel = new FurniturePanel( testFurniture9, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)
    val bookshelfPanel = new FurniturePanel(testFurniture10, (stage.width.toDouble / 4), (spaceForBottom / 3), furniturePane, allFurniture)

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
          wallsInFloorPlan.clear()
          amountOfLamps = 0
          furniturePane.children.clear()
          stack.children.clear()
          stack.children = Array( mainScene, furniturePane)
          scaleInput(allFurniture, stack, stage).show()


    /** Kutsutaan metodia saveButtonMaker, tallennusnappien luomiseksi*/
    val save1 = saveButtonMaker(stage, stack)
    val bottomBar = bottomBarMaker()

    /** Nappi, joka tyhjentää tämänhetkisen näkymän kaikista huonekaluista*/
    val restartButton = new Button( "Restart" ):
      onAction = (event) =>
        furniturePane.children.clear()
        allFurniture.clear()
        allFurniture.addAll( wallsInFloorPlan )
        amountOfLamps = 0

    val scaleButton1 = new Button("Change scale"):
      onAction = (event) =>
        scaleInput(allFurniture, stack, stage).show()


    /** Nappi, jota painamalla käyttäjä pääsee luomaan oman pojapiirustuksensa */
    val designYourOwnButton = new Button( "Design your own floorplan"):
      onAction = (event) =>
        scene1.root = root2

    /** POHJAPIIRUSTUKSEN SUUNNITTELUNÄKYMÄN NAPIT */

    val save2 = saveButtonMaker(stage, stack2)

    /** Kun käyttäjä painaa tätä nappia, hän käyttää tämän hetkistä pohjapiirustustaan.
     * Pohjapiirustuksen luomisnäkymä nollataan */
    val useThisFloorPlanButton = new Button( "Use this floorplan" ):
      onAction = (event) =>
        wallsInFloorPlan.clear()
        allFurniture.clear()
        allFurniture.addAll( allWalls)
        wallsInFloorPlan.addAll( allWalls )
        val writable = new WritableImage(mainScene.getFitWidth.toInt, mainScene.getFitHeight.toInt)
        stack2.snapshot(null, writable)
        val image = sfxImage2jfx(writable)
        mainScene = new ImageView(image):
            fitHeight = spaceForBottom
            fitWidth = stage.width.toDouble * 3/4
        allWalls.clear()
        floorplanDesignPane.children.clear()
        stack.children.clear()
        stack.children = Array(mainScene, furniturePane)
        scene1.root = root

    val bottomBar2 = bottomBarMaker()

    /** Poistaa kaikki pohjapiirustuksen luomisnäkymässä olevat huonekalut */
    val restartButton2 = new Button( "Restart" ):
      onAction = (event) =>
        floorplanDesignPane.children.clear()
        allWalls.clear()

    val scaleButton2 = new Button("Change scale"):
      onAction = (event) =>
        scaleInput(allWalls, stack2, stage).show()


   /** Käyttäjä voi palata takaisin jatkamaan huonekalujen lisäämistä aikaisempaan
    * pohjapiirustukseensa pohjapiirustuksen luomisnäkymästä. */
    val returnButton = new Button( "Go back"):
      onAction = (event) =>
        scene1.root = root

    /** Asetetaan lapset */

    bottomBar.children = Array(addButton, save1, designYourOwnButton, restartButton, scaleButton1)

    sidePanel.children = Array(lampPanel2, tablePanel, diningTablePanel, chairPanel, sofaPanel, sofaTablePanel, bookshelfPanel, bedPanel, rugPanel, roundRugPanel )

    root.children = Array(mainView, bottomBar)

    bottomBar2.children = Array(returnButton, save2, restartButton2, useThisFloorPlanButton, scaleButton2)

    sidePanel2.children = Array( wallPanel, doorPanel, windowPanel, counterPanel )

    root2.children = Array(secondView, bottomBar2)




  end start


  /** Metodi, joka tekee uuden nappulan, jolla voi tallentaa tietyn stackPanen kuvana */
  def saveButtonMaker(stage: Stage, stack: StackPane): Button = new Button( "Save"):
    onAction = (event) =>
      /** Luodaan uusi filechooser ja lisätään sille filttereiksi PNG ja JPEG, jotta objekti tallentuu kuva
       * tiedostoon.*/
      val fileChooser = new FileChooser():
        extensionFilters.add( ExtensionFilter("PNG", "*.png") )
      fileChooser.setTitle("Save file")
      val fileToSave = fileChooser.showSaveDialog(stage)
      if fileToSave != null then
        /** Tehdään uusi WritableImage, johon voidaan sitten tallentaa stackin tiedot */
        var writable = new WritableImage( stack.getWidth.toInt, stack.getHeight.toInt)
        stack.snapshot(null, writable)
        var rendered = SwingFXUtils.fromFXImage(writable, null)
        ImageIO.write(rendered, "png", fileToSave)
  end saveButtonMaker


  /** Metodi, joka laatii alapalkin. Vähentää koodin toisteisuutta*/
  def bottomBarMaker(): HBox =
    new HBox():
      val stroke = BorderStroke(Black, BorderStrokeStyle.Solid, CornerRadii(1), BorderWidths(5))
        padding = Insets.apply(17.5, 17.5, 17.5, 17.5)
        spacing = 10
        background = Background.fill(Pink)
        border = Border( Array(stroke), Array[BorderImage]())
  end bottomBarMaker

  /** Metodi, joka mahdollistaa pohjapiirustuksen skaalan muuttamiseen, eli siis käyttäjä voi muuttaa pohjapiirustuksen
   * leveyttä ja korkeutta, jolloin skaala muuttuu myös*/
  def scaleInput(l: ListBuffer[Furniture], s: StackPane, s2: Stage): Stage =

    val stage = new Stage()
    stage.setWidth(300)
    stage.setHeight(300)
    stage.setX(300)
    stage.setY(300)

    val pane = new VBox()

    val label1 = new Label("The width of your floorplan")
    val label2 = new Label( "The height of your floorplan")
    val label3 = new Label( "m")
    val label4 = new Label( "m")

    val widthField = new Spinner[Double](1, 20, 5, 0.5)
    val heightField = new Spinner[Double](1, 20, 5, 0.5)

    val scalingWidth = new HBox():
      spacing = 5
      children = Array(widthField, label3)

    val scalingHeight = new HBox():
      spacing = 5
      children = Array(heightField, label4)

    val submit = new Button( "Submit"):
      onAction = (event) =>
        /** Muutetaan skaalaa ja skaalataan kaikki olemassa olevat huonekalut kyseiseen skaalaan */
        floorPlanScaleX = (1000 * 3/4) / (widthField.getValue * 100)
        floorPlanScaleY = (600 * 4/5 ) / (heightField.getValue * 100)
        stage.close()
        l.foreach(x => x.shape.setScaleX(floorPlanScaleX))
        l.foreach(x => x.shape.setScaleY(floorPlanScaleY))

    pane.children = Array( label1, scalingWidth, label2, scalingHeight, submit)

    val scene = new Scene(pane)
    stage.setScene(scene)
    stage

  end scaleInput

end DesingGUI
