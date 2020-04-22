package com.example.cricketscorekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    int teamAScore;
    int teamABalls;
    int teamAWickets;
    int teamBScore;
    int teamBBalls;
    int teamBWickets;
    String lastActionTeam;
    int lastActionScoreIncrement;
    int lastActionBallIncrement;
    int lastActionWicketIncrement;
    int additionalRuns;
    String currentTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        teamAScore = 0;
        teamABalls = 0;
        teamAWickets = 0;
        teamBScore = 0;
        teamBBalls = 0;
        teamBWickets = 0;
        currentTeam = "Team A";
        additionalRuns = 4;
        clearUndoMemory();
    }

    public void handleScoringButtonClick(View view) {
        Button button = (Button) view;
        int scoreIncrement = Integer.parseInt((String) button.getText());
        int ballIncrement = 1;
        incrementScoring(currentTeam, scoreIncrement, ballIncrement, 0);
        updateDisplay();
    }

    private void incrementScoring(
            String team,
            int scoreIncrement,
            int ballIncrement,
            int wicketIncrement) {
        switch (team) {
            case "Team A":
                teamAScore += scoreIncrement;
                teamABalls += ballIncrement;
                teamAWickets += wicketIncrement;
                break;
            case "Team B":
                teamBScore += scoreIncrement;
                teamBBalls += ballIncrement;
                teamBWickets += wicketIncrement;
                break;
        }
        lastActionTeam = team;
        lastActionScoreIncrement = scoreIncrement;
        lastActionBallIncrement = ballIncrement;
        lastActionWicketIncrement = wicketIncrement;
    }

    private void updateDisplay() {
        TextView teamAScoreView = (TextView) findViewById(R.id.team_a_score);
        TextView teamAOversView = (TextView) findViewById(R.id.team_a_overs);
        TextView teamARequiredRunRate = (TextView) findViewById(R.id.team_a_rrr);
        TextView teamACurrentRunRate = (TextView) findViewById(R.id.team_a_crr);
        TextView teamBScoreView = (TextView) findViewById(R.id.team_b_score);
        TextView teamBOversView = (TextView) findViewById(R.id.team_b_overs);
        TextView teamBRequiredRunRate = (TextView) findViewById(R.id.team_b_rrr);
        TextView teamBCurrentRunRate = (TextView) findViewById(R.id.team_b_crr);
        teamAScoreView.setText(teamAScore + "/" + teamAWickets);
        teamBScoreView.setText(teamBScore + "/" + teamBWickets);
        teamAOversView.setText(calculateOvers(teamABalls));
        teamBOversView.setText(calculateOvers(teamBBalls));

    }

    private String calculateOvers(int balls) {
        int wholeNumber = (int) Math.floor(balls / 6);
        int decimal = balls % 6;
        return ("(" + wholeNumber + "." + decimal + ")");
    }

    public void toggleMenu(View view) {
        View menu = (View) findViewById(R.id.expanded_menu);
        switch (menu.getVisibility()) {
            case View.GONE:
                menu.setVisibility(View.VISIBLE);
                break;
            case View.VISIBLE:
                menu.setVisibility(View.GONE);
        }
    }

    public void toggleMenu() {
        View menu = (View) findViewById(R.id.expanded_menu);
        switch (menu.getVisibility()) {
            case View.GONE:
                menu.setVisibility(View.VISIBLE);
                break;
            case View.VISIBLE:
                menu.setVisibility(View.GONE);
        }
    }

    public void togglePopup(View view) {
        Button button = (Button) view;
        String buttonTag = (String) button.getTag();
        togglePopup(buttonTag);
    }

    private void togglePopup(String buttonTag) {
        View popup = (View) findViewById(R.id.popup);
        int toggleAction = View.VISIBLE;
        switch (popup.getVisibility()) {
            case View.GONE:
                toggleAction = View.VISIBLE;
                break;
            case View.VISIBLE:
                toggleAction = View.GONE;
                break;
        }

        popup.setVisibility(toggleAction);

        switch (buttonTag) {
            case "additional_runs":
                View additionalRunsView = (View) findViewById(R.id.additional_runs);
                additionalRunsView.setVisibility(toggleAction);
                additionalRuns = 4;
                TextView additionalRunsValue = (TextView) findViewById(R.id.additional_runs_value);
                additionalRunsValue.setText(String.valueOf(additionalRuns));
                break;
            case "extra":
                View extrasOptionsView = (View) findViewById(R.id.extras_options);
                extrasOptionsView.setVisibility(toggleAction);
                break;
            case "wicket":
                View wicketOptionsView = (View) findViewById(R.id.wicket_options);
                wicketOptionsView.setVisibility(toggleAction);
                break;
        }
    }

    public void incrementAdditionalRuns(View view) {
        int increment = Integer.parseInt((String) view.getTag());
        additionalRuns += increment;
        if (additionalRuns < 0) {
            additionalRuns = 0;
        }
        TextView additionalRunsValue = (TextView) findViewById(R.id.additional_runs_value);
        additionalRunsValue.setText(String.valueOf(additionalRuns));
    }

    public void handleSubmitAdditionalRuns(View view) {
        TextView additionalRunsView = (TextView) findViewById(R.id.additional_runs_value);
        int scoreIncrement = Integer.parseInt((String) additionalRunsView.getText());
        incrementScoring(currentTeam, scoreIncrement, 1, 0);
        updateDisplay();
        togglePopup("additional_runs");
    }

    public void handleExtraButtonClick(View view) {
        incrementScoring(currentTeam, 1, 0, 0);
        updateDisplay();
        togglePopup("extra");
    }

    public void handleWicketButtonClick(View view) {
        incrementScoring(currentTeam, 0, 1, 1);
        updateDisplay();
        togglePopup("wicket");
    }

    public void undo(View view) {
        incrementScoring(
                lastActionTeam,
                lastActionScoreIncrement * -1,
                lastActionBallIncrement * -1,
                lastActionWicketIncrement * -1);

        clearUndoMemory();
        updateDisplay();
    }

    private void clearUndoMemory() {
        lastActionTeam = "";
        lastActionScoreIncrement = 0;
        lastActionBallIncrement = 0;
        lastActionWicketIncrement = 0;
    }

    public void endInnings(View view) {
        switchTeam();
        toggleMenu();
    }

    private void switchTeam() {
        View teamAColumn = (View) findViewById(R.id.team_a_column);
        LinearLayout.LayoutParams layoutAParams =
                (LinearLayout.LayoutParams)  teamAColumn.getLayoutParams();
        View teamBColumn = (View) findViewById(R.id.team_b_column);
        LinearLayout.LayoutParams layoutBParams =
                (LinearLayout.LayoutParams)  teamBColumn.getLayoutParams();
        View teamABatting = (View) findViewById(R.id.team_a_batting);
        View teamBBatting = (View) findViewById(R.id.team_b_batting);

        switch (currentTeam) {
            case "Team A":
                currentTeam = "Team B";
                layoutAParams.weight = 36;
                layoutBParams.weight = 100;
                teamABatting.setVisibility(View.GONE);
                teamBBatting.setVisibility(View.VISIBLE);
                break;
            case "Team B":
                currentTeam = "Team A";
                layoutAParams.weight = 100;
                layoutBParams.weight = 36;
                teamABatting.setVisibility(View.VISIBLE);
                teamBBatting.setVisibility(View.GONE);
                break;
        }
        teamAColumn.setLayoutParams(layoutAParams);
        teamBColumn.setLayoutParams(layoutBParams);
    }

    public void reset(View view) {
        teamAScore = 0;
        teamABalls = 0;
        teamAWickets = 0;
        teamBScore = 0;
        teamBBalls = 0;
        teamBWickets = 0;
        updateDisplay();

        clearUndoMemory();

        if (currentTeam.equals("Team B")) {
            switchTeam();
        }

        toggleMenu();
    }
}
