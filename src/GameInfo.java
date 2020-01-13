public class GameInfo {
    private static int hitCountMaxIdx;

    static {
        hitCountMaxIdx = 2;
    }

    private int score;
    private int comboMax;
    private int[] hitCount;

    public GameInfo() {
        score = 0;
        comboMax = 0;
        hitCount = new int[] {0, 0, 0};
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        if(score >= 0) {
            this.score = score;
        } else {
            throw new GameInfoException("Error: score value is negative");
        }
    }

    public int getComboMax() {
        return comboMax;
    }
    public void setComboMax(int comboMax) {
        if(comboMax >= 0 && comboMax > this.comboMax) {
            this.comboMax = comboMax;
        } else if(comboMax > 0) {
            throw new GameInfoException("Error: comboMax value is negative");
        } else {
            throw new GameInfoException("Error: comboMax value is less than previous value");
        }
    }

    public int[] getHitCount() {
        return hitCount;
    }
    public void setHitCount(int index, int hitCount) {
        if(index > hitCountMaxIdx) {
            throw new GameInfoException("Error: index of hitCount is not valid");
        } else {
            if(hitCount > this.hitCount[index]) {
                this.hitCount[index] = hitCount;
            } else {
                throw new GameInfoException("Error: value for hitCount[" + index + "] is less than previous value");
            }
        }
    }

    public void resetInfo() {
        score = 0;
        comboMax = 0;

        for(int curr : hitCount) { //TODO: hitCount가 0으로 설정 되는지 확인할 것
            curr = 0;
        }
    }

}