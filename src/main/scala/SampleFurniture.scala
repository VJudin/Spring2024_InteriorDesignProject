import scalafx.scene.image.Image
import scalafx.scene.paint.Color.{Beige, Black, Blue, Brown, DarkSeaGreen, ForestGreen, LightCoral, LightCyan, RosyBrown, White, Yellow}
import scalafx.scene.shape.{Circle, Ellipse, Rectangle}

import java.io.FileInputStream

/** Test furniture that allow for the furniture panels to be crated which are shown to the user */
object testFurniture:
    val testSofa = new Furniture(
      "Sofa",
      200,
      100,
      true,
      Rectangle(200, 100),
      300,
      300,
      DarkSeaGreen,
      false,
      Image(FileInputStream("src/main/Pictures/sohva.jpeg"))
    )
    
    val testRoundTable = new Furniture(
      "Round Table",
      150,
      150,
      true,
      Circle(50),
      200,
      200,
      Blue,
      false,
      Image(FileInputStream("src/main/Pictures/table.jpeg"))
    )
    
    val testCoffeeTable = new Furniture(
      "Coffee table",
      100,
      50,
      true,
      Ellipse(100, 100, 30, 20),
      100,
      100,
      ForestGreen,
      false,
      Image(FileInputStream("src/main/Pictures/coffeeTable.jpeg"))
    )
    
    val testRug= new Rug(
      100,
      100,
      100,
      100,
      Rectangle(100, 100),
      LightCyan,
      Image(FileInputStream("src/main/Pictures/Rug.jpeg"))
    )
    
    val testRug2 = new Rug(
      50,
      50,
      100,
      100,
      Circle(50),
      LightCoral,
      Image(FileInputStream("src/main/Pictures/roundRug.jpeg"))
    )
    
    val testBed = new Furniture(
      "Bed",
      200,
      180,
      true,
      Rectangle(200, 180),
      300,
      300,
      Beige,
      false,
      Image(FileInputStream("src/main/Pictures/bed.jpeg"))
    )
    
    val testTable = new Furniture(
      "Table",
      200,
      150,
      true,
      Rectangle(200, 150),
      300,
      300,
      Brown,
      false,
      Image(FileInputStream("src/main/Pictures/diningTable.jpeg"))
    )
    
    val testChair = new Furniture(
      "Chair",
      50,
      50,
      true,
      Rectangle(50, 50),
      300,
      300,
      Black,
      false,
      Image(FileInputStream("src/main/Pictures/chair.jpeg"))
    )
    
    val testBookshelf = new Furniture(
      "Bookshelf",
      200,
      50,
      true,
      Rectangle(200, 50),
      300,
      300,
      RosyBrown,
      false,
      Image(FileInputStream("src/main/Pictures/bookshelf.jpeg"))
    )
    
    val testLamp = new Lamp(20, 100, 100, Yellow)
    
    val testWall = new Wall(200, 10, 300, 300, Black)
    
    val testDoor = new Door(100, 100, 300, 300, White)
    
    val testWindow = new Window(100, 10, 300, 300)
    
    val testCounter = new Counter(100, 100, 300, 300, Black)
