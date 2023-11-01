package domain;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomNumberInRangeTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import camp.nextstep.edu.missionutils.test.NsTest;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import racingcar.Application;
import racingcar.domain.Attempt;
import racingcar.domain.Car;
import racingcar.domain.Cars;
import racingcar.domain.Game;
import racingcar.domain.Umpire;

public class GameTest extends NsTest {
    private static final int NOT_MOVE = 0;
    private static final int MOVING_FORWARD = 4;
    private static final int STOP = 3;
    private final String[] names = new String[] {"pobi", "woni", "jun"};

    @Test
    void 자동차_이름을_쉼표로_구분하여_입력받기() {
        Cars cars = new Cars();
        String input = "pobi,woni,jun";
        command(input);

        List<String> carNames = cars.inputCarNames();

        assertThat(carNames).containsExactly("pobi", "woni", "jun");
    }

    @Test
    void 시도_횟수_입력받아_저장하기() {
        Attempt attempt = new Attempt();
        command("5");

        assertThatCode(() -> attempt.saveAttemptNumber()).doesNotThrowAnyException();
    }

    @Test
    void 아무도_움직이지_않았을_경우() {
        assertRandomNumberInRangeTest(
                () -> {
                    run("pobi,woni", "2");
                    assertThat(output()).contains("pobi : ", "woni : ", "pobi : ", "woni : ", "최종 우승자 :");
                },
                NOT_MOVE, NOT_MOVE
        );
    }

    @Test
    void 먀먀() {
        List<Car> cars = Arrays.stream(names)
                .map(Car::new)
                .collect(Collectors.toList());

        assertRandomNumberInRangeTest(
                () -> {
                    Umpire umpire = new Umpire(cars);
                    String winnerResult = umpire.findWinner();

                    assertEquals("pobi, woni", winnerResult);
                },
                MOVING_FORWARD, STOP, MOVING_FORWARD
        );
    }

    @Test
    void 우승자_찾기() {
        List<Car> cars = Arrays.stream(names)
                .map(Car::new)
                .collect(Collectors.toList());

        assertRandomNumberInRangeTest(
                () -> {
                    Umpire umpire = new Umpire(cars);
                    Game game = new Game(cars, 1);
                    game.play();
                    String winnerResult = umpire.findWinner();

                    assertEquals("pobi, woni", winnerResult);
                },
                MOVING_FORWARD, MOVING_FORWARD, STOP
        );
    }

    @Test
    void 각_라운드_결과_가져오기() {
        List<Car> cars = new ArrayList<>();
        Car car1 = mock(Car.class);
        Car car2 = mock(Car.class);
        Car car3 = mock(Car.class);

        cars.add(car1);
        cars.add(car2);
        cars.add(car3);

        when(car1.getName()).thenReturn("pobi");
        when(car2.getName()).thenReturn("woni");
        when(car3.getName()).thenReturn("jun");
        when(car1.getPosition()).thenReturn(1);
        when(car2.getPosition()).thenReturn(2);
        when(car3.getPosition()).thenReturn(3);

        Game game = new Game(cars, 5);
        List<String> roundResult = game.getRoundResult();

        assertEquals("pobi : -,woni : --,jun : ---", String.join(",", roundResult));
    }

    private void command(final String... args) {
        final byte[] buf = String.join("\n", args).getBytes();
        System.setIn(new ByteArrayInputStream(buf));
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }

}
