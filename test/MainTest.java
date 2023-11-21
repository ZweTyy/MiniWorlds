import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testMain() {
        // Test case 1: Verify that the main method runs without any exceptions
        assertDoesNotThrow(() -> Main.main(new String[] {}));

    }

    @Test
    public void testNoSpaceLeft() {
        // Assuming World and Person are classes in your program
        // and World has a method canPlacePerson() that returns false when there's no
        // space left
        // and a method placePerson() that throws an exception when there's no space
        // left

        World world = new World();
        Person person = new Person();

        while (world.canPlacePerson()) {
            world.placePerson(person);
        }
        assertThrows(Exception.class, () -> world.placePerson(person));
    }
}
