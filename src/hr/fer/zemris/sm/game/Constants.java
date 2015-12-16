package hr.fer.zemris.sm.game;

/**
 * Created by doctor on 02.12.15..
 */
public class Constants {

    public static final String GAME_TITLE = "NeuroAsteroids";
    public static final String GAME_TITLE_LABEL = "titleLabel";

    public static final String GAME_ICON = "res/images/Meteor-icon.png";

    public static final String GAME_STYLE_PATH = "res/style.css";
    public static final String CURSOR_IMG_PATH = "res/images/aimCursorWhite.png";
    public static final String GAME_WORLD_STYLE_PATH = "res/gameStyle.css";

    public static final String BACK_BUTTON_TEXT = "Back";

    public static final String HIGH_SCORE_FILE = "res/hs";
    public static final int MAX_HIGH_SCORE_LIST_SIZE = 100;
    public static final String SCORE_ELEMENT_TYPE_AI = "AI";
    public static final String SCORE_ELEMENT_TYPE_HUMANE = "HUMAN";

    public static final String EVOLUTION_ELEMENTS_LIST_FILE = "res/elements";
    public static final String EVOLUTION_ELEMENTS_FILES_DIR= "res/saved/";
    public static final String CREDITS_PATH = "res/credits.txt";

    public static final double OUT_TRANSITION_DURNATION = 0.25;
    public static final double IN_TRANSITION_DURNATION  = 0.25;

    public static final int HUMAN_PLAY_ASTEROIDS_NUMEBER = 10;

    public static final int AI_GRAPHIC_PLAY_ASTEROIDS_NUMBER = 15;

    public static final int AI_SIMULATION_PLAY_ASTEROIDS_NUMBER = 15;
    
    public static final int STARS_NUMBER = 15;

    public static final String FITTNESS_FORMAT = "#.###";

    //*************************************//
    //               Audio                 //
    //*************************************//
    public static final double INIT_BACKGROUND_SOUND_VOLUME = 0.1;
    public static final double INIT_EFFECT_SOUND_VOLUME = 0.1;

    public static final String BACKGROUND_MUSIC = "res/audio/background_music.mp3";
    public static final String EXPLOSION_CLIP = "res/audio/explosion_punchy_impact_02.mp3";
    public static final String FIRE_CLIP = "res/audio/fireSound.wav";
    public static final String SHIP_EXPLODED_CLIP = "res/audio/shipExploded.mp3";


    //*************************************//
    //             Start menu              //
    //*************************************//
    public static final String START_MENU_ID = "startMenu";
    public static final String START_MENU_BUTTONS_DIV = "startMenuButtons";

    public static final String PLAY_BUTTON = "playButton";
    public static final String PLAY_BUTTON_TEXT = "PLAY";

    public static final String SCORES_BUTTON = "scoresButton";
    public static final String SCORES_BUTTON_TEXT = "SCORES";

    public static final String OPTIONS_BUTTON = "optionsButton";
    public static final String OPTIONS_BUTTON_TEXT = "OPTIONS";

    public static final String CREDITS_BUTTON = "creditsButton";
    public static final String CREDITS_BUTTON_TEXT = "CREDITS";

    public static final String EXIT_BUTTON = "exitButton";
    public static final String EXIT_BUTTON_TEXT = "EXIT";


    //*************************************//
    //             Play menu               //
    //*************************************//
    public static final String PLAY_MENU_ID = "playMenu";
    public static final String PLAY_MENU_BUTTONS = "playMenuButtons";

    public static final String HUMAN_PLAY_BTN_TEXT = "Play human";
    public static final String AI_PLAY_BTN_TEXT = "Play AI";


    //*************************************//
    //             Score menu              //
    //*************************************//
    public static final String SCORE_MENU_ID = "scoreMenu";
    public static final String SCORE_MENU_TABLE = "scoreMenuTable";
    public static final String SCORE_MENU_BACK_BTN = "scoreMenuBackBtn";

    public static final String TYPE = "Type";
    public static final String SCORE_NICK = "Nick";
    public static final String SCORE = "Score";

    //*************************************//
    //           Credits menu              //
    //*************************************//
    public static final String CREDITS_PROJECT_NAME_LABEL_TEXT = "Project NeuroAsteroids (NA)";
    public static final String CREDITS_PROJECT_NAME_LABEL = "creditsProjectNameLabel";
    public static final String CREDITS_CONTENT_VBOX = "creditsContentVBox";
    public static final String CREDITS_BACK_BUTTON = "creditsMenuBackBtn";
    public static final String CREDITS_CONTENT_GRID = "creditsContentGrid";
    public static final String CREDITS_LEADER = "Leader";
    public static final String CREDITS_MENTOR = "Mentor";
    public static final String CREDITS_TEAM   = "Team";
    public static final String CREDITS_LEADER_LABEL_TEXT = "Team leader: ";
    public static final String CREDITS_MENTOR_LABEL_TEXT = "Project mentor: ";
    public static final String CREDITS_TEAM_LABEL_TEXT   = "Team: ";

    //*************************************//
    //           Options menu              //
    //*************************************//
    public static final String OPTIONS_PANE = "optionsPane";
    public static final String AUDIO_LABEL = "audioLabel";
    public static final String AUDIO_LABEL_TEXT = "AUDIO";
    public static final String VIDEO_LABEL = "videoLabel";
    public static final String VIDEO_LABEL_TEXT = "VIDEO";
    public static final String BACKGROUND_SOUND_LABEL_TEXT = "Background sound";
    public static final String EFFECT_SOUND_LABEL_TEXT = "Effect sound";
    public static final String AUDIO_OPTIONS_GRID = "audioOptionsGrid";
    public static final String OPTIONS_MENU_BACK_BUTTON = "optionsMenuBackBtn";
    public static final String CONTROL_LABEL_TEXT = "CONTROLS";
    public static final String CONTROL_LABEL = "controlLabel";
    public static final String CONTROL_OPTIONS_GRID = "controlOptionsGrid";
    public static final String CONTROL_INPUT_LABEL_TEXT = "INPUT";
    public static final String ARROW_CONTROL_INPUT_LABEL_TEXT = "Arrows";
    public static final String WASD_CONTROL_INPUT_LABEL_TEXT = "W_A_D_SPACE (Linux users)";

    //*************************************//
    //          AIChooser menu             //
    //*************************************//
    public static final String NAME_HBOX_ID = "nameHBoxId";
    public static final String NAME_LABEL = "nameLabel";

    public static final String FITT_HBOX_ID = "fitHBoxId";
    public static final String FITT_LABEL = "fitLabel";

    public static final String COMM_VBOX_ID = "commVBoxId";
    public static final String COMM_LABEL = "commLabel";

    public static final String CONTENT_PANE = "contentPane";
    public static final String RIGHT_PANE = "rightPane";
    public static final String RIGHT_PANE_LABELS = "rightPaneLabels";
    public static final String LEFT_PANE = "leftPane";
    public static final String AI_LIST = "aiList";
    public static final String LIST_ITEM = "listItem";

    //*************************************//
    //             Pause menu              //
    //*************************************//
    public static final String PAUSE_MENU = "pauseMenu";
    public static final String PAUSE_PANE = "pausePane";

    public static final String PAUSE_PANE_SLIDERS = "pauseAudioSliderVBox";
    public static final String PAUSE_PANE_BUTTONS = "pausePaneButtons";

    //Buttons
    public static final String PAUSE_MENU_RESUME_BUTTON = "pauseMenuResumeBtn";
    public static final String PAUSE_MENU_RESUME_BUTTON_TEXT = "Resume";
    public static final String PAUSE_MENU_RESTART_BUTTON = "pauseMenuRestart";
    public static final String PAUSE_MENU_RESTART_BUTTON_TEXT = "Restart";
    public static final String PAUSE_MENU_EXIT_BUTTON = "pauseMenuExitBtn";
    public static final String PAUSE_MENU_EXIT_BUTTON_TEXT = "Exit to menu";

    //*************************************//
    //          Game over screen           //
    //*************************************//
    public static final String GAME_OVER_SCREEN = "gameOverScreen";
    public static final String GAME_OVER_BUTTON_BOX = "gameOverButtonBox";
    public static final String GAME_OVER_CONTENT_BOX= "gameOverContentBox";
    public static final String GAME_OVER_LABEL = "gameOverLabel";
    public static final String GAME_OVER_LABEL_TEXT = "GAME OVER";
    public static final String GAME_OVER_RESET_BUTTON = "gameOverResetBtn";
    public static final String GAME_OVER_RESET_BUTTON_TEXT = "Reset";
    public static final String GAME_OVER_TO_MENU_BUTTON = "gameOverToMenuButton";
    public static final String GAME_OVER_TO_MENU_BUTTON_TEXT = "To menu";

    //*************************************//
    //          High score screen          //
    //*************************************//
    public static final String HIGH_SCORE_SCREEN = "highScoreScreen";
    public static final String HIGH_SCREEN_CONTENT_BOX = "highScreenContentBox";
    public static final String HIGH_SCORE_LABEL = "hsLabel";
    public static final String HIGH_SCORE_LABEL_TEXT = "HIGH SCORE";
    public static final String SCORE_LABEL_TEXT = " POINTS";
    public static final String SCORE_LABEL_TEXT_ONE_POINTS = " POINT";
    public static final String SCORE_LABEL = "scoreLabel";

    public static final String PLACE_TEXT = " PLACE";
    public static final String PLACE_LABEL = "placeLabel";
    public static final String NAME_TEXT_FIELD = "nameTextField";
    public static final String ENTERED_SCORE_BUTTON = "enteredScoreButton";
    public static final String ENTERED_SCORE_BUTTON_TEXT = "Save score";
    public static final String WRONG_INPUT_LABEL = "wrongInputLabel";
    public static final String NAME_TOO_LONG_TEXT = "Name is too long";
    public static final String NAME_EMPTY_TEXT = "Pleas enter some name";
    public static final String NAME_TAKEN_TEXT = "Name is already taken";
    public static final String SCORE_INPUT_HBOX = "scoreInputBox";

    public static final int MAX_NAME_LENGTH = 20;

    //*************************************//
    //              Game                   //
    //*************************************//
    public static final String EXPLOSION_ANIMATION_PATH = "res/images/exp2.png";
    public static final String ASTEROID_ANIMATION_PATH  = "res/images/rocks.png";
    public static final String STAR_ANIMATION_PATH = "res/images/starFrames.png";
    public static final String MISSILE_IMAGE_PATH = "res/images/miss.gif";
    public static final String SHIP_IMAGE_PATH = "res/images/spaceship.png";

}
