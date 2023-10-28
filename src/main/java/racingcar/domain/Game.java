package racingcar.domain;

import static racingcar.constant.GameMessage.COLON;
import static racingcar.constant.GameMessage.DASH;
import static racingcar.constant.GameMessage.DELIMITER_WINNER;
import static racingcar.constant.GameNumber.STOP_ATTEMPT_NUMBER;

import java.util.ArrayList;
import java.util.List;
import racingcar.view.OutputView;

public class Game {
    private final Movement move;
    private final List<Car> cars;
    private int attemptNumber;

    public Game(List<Car> cars, int attemptNumber) {
        this.cars = cars;
        this.attemptNumber = attemptNumber;
        move = new Movement();
    }

    public void startRacing() {
        playRacing();
        showWinner();
    }

    public void playRacing() {
        OutputView.printResultMessage();
        while (attemptNumber != STOP_ATTEMPT_NUMBER) {
            move.moveCar(cars);
            OutputView.printRoundResult(getRoundResult());
            attemptNumber--;
        }
    }

    public List<String> getRoundResult() {
        List<String> roundResult = new ArrayList<>();

        for (Car car : cars) {
            String moveDash = DASH.repeat(car.getPosition());
            roundResult.add(car.getName() + COLON + moveDash);
        }

        return roundResult;
    }

    public void showWinner() {
        OutputView.printFinalWinner(listWinner());
    }

    public String listWinner() {
        List<String> winners = findWinner();

        return String.join(DELIMITER_WINNER, winners);
    }

    public int findMaxPosition() {
        int maxPosition = 0;
        for (Car car : cars) {
            maxPosition = Math.max(maxPosition, car.getPosition());
        }

        return maxPosition;

    }

    public List<String> findWinner() {
        List<String> winners = new ArrayList<>();
        int maxPosition = findMaxPosition();

        for (Car car : cars) {
            if (car.getPosition() == maxPosition) {
                winners.add(car.getName());
            }
        }

        return winners;
    }

}
