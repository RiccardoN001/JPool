package game.utils;

// BRIEF CLASS DESCRIPTION
// Contains constants relative to the main components of the game

public class Constants {

    // BALL SIZE
    public static final double BALL_RADIUS = 12.5;
    public static final double BALL_DIAMETER = 2 * BALL_RADIUS;
    public static final double GHOSTBALL_RADIUS = 10;

    // TABLE FRICTION
    public static final double TABLE_FRICTION = .99;
    
    // SPLIT
    // HEAD SPOT
    public static final double HEAD_SPOT_X = 500;
    public static final double HEAD_SPOT_Y = 465;
    // TRIANGLE
    // 5 possible layouts on the x-axis
    public static final double TRIANGLE_ROW1_X = 923;
    public static final double TRIANGLE_ROW2_X = 947;
    public static final double TRIANGLE_ROW3_X = 971;
    public static final double TRIANGLE_ROW4_X = 995;
    public static final double TRIANGLE_ROW5_X = 1019;
    // 9 possible layouts on the y-axis
    public static final double TRIANGLE_COL1_Y = 415;
    public static final double TRIANGLE_COL2_Y = 428;
    public static final double TRIANGLE_COL3_Y = 440;
    public static final double TRIANGLE_COL4_Y = 453;
    public static final double TRIANGLE_COL5_Y = 465;
    public static final double TRIANGLE_COL6_Y = 478;
    public static final double TRIANGLE_COL7_Y = 490;
    public static final double TRIANGLE_COL8_Y = 503;
    public static final double TRIANGLE_COL9_Y = 515;
    // FOOT SPOT
    public static final double FOOT_SPOT_X = TRIANGLE_ROW1_X;
    public static final double FOOT_SPOT_Y = TRIANGLE_COL5_Y;

    // SCOREBALLS RACKS
    public static final double RACK_LEFT = 253;
    public static final double RACK_RIGHT = 882;

    // BANKCOLLISION
    public static final double A_MARGIN = 300;
    public static final double B_MARGIN = 1108;
    public static final double CD_MARGIN = 259;
    public static final double EF_MARGIN = 669;
    // LEFT BANK (A)
    public static final double A_UP_CORNER_START = 268;
    public static final double A_UP_CORNER_END = 286;
    public static final double A_DOWN_CORNER_START = 637;
    public static final double A_DOWN_CORNER_END = 650;
    // RIGHT BANK (B)
    public static final double B_UP_CORNER_START = 270;
    public static final double B_UP_CORNER_END = 282;
    public static final double B_DOWN_CORNER_START = 641;
    public static final double B_DOWN_CORNER_END = 660;
    // LEFT UP BANK (C)
    public static final double C_LEFT_CORNER_START = 307;
    public static final double C_LEFT_CORNER_END = 328;
    public static final double C_RIGHT_CORNER_START = 669;
    public static final double C_RIGHT_CORNER_END = 680;
    // RIGHT UP BANK (D)
    public static final double D_LEFT_CORNER_START = 722;
    public static final double D_LEFT_CORNER_END = 738;
    public static final double D_RIGHT_CORNER_START = 1082;
    public static final double D_RIGHT_CORNER_END = 1098;
    // LEFT DOWN BANK (E)
    public static final double E_LEFT_CORNER_START = 307+10;
    public static final double E_LEFT_CORNER_END = 324+10;
    public static final double E_RIGHT_CORNER_START = 669;
    public static final double E_RIGHT_CORNER_END = 683;
    // RIGHT DOWN BANK (F)
    public static final double F_LEFT_CORNER_START = 722;
    public static final double F_LEFT_CORNER_END = 734;
    public static final double F_RIGHT_CORNER_START = 1078;
    public static final double F_RIGHT_CORNER_END = 1099;

    // POCKETS
    //TOP-LEFT POCKET
    public static final double TOP_LEFT_POCKET_X = 309;
    public static final double TOP_LEFT_POCKET_Y = 269;
    //BOTTOM-LEFT POCKET
    public static final double BOTTOM_LEFT_POCKET_X = 309;
    public static final double BOTTOM_LEFT_POCKET_Y = 710;
    //TOP-MIDDLE POCKET
    public static final double TOP_MIDDLE_POCKET_X = 714;
    public static final double TOP_MIDDLE_POCKET_Y = 261;
    //BOTTOM-MIDDLE POCKET
    public static final double BOTTOM_MIDDLE_POCKET_X = 714;
    public static final double BOTTOM_MIDDLE_POCKET_Y = 690;
    //TOP-RIGHT POCKET
    public static final double TOP_RIGHT_POCKET_X = 1149;
    public static final double TOP_RIGHT_POCKET_Y = 269;
    //BOTTOM-RIGHT POCKET
    public static final double BOTTOM_RIGHT_POCKET_X = 1149;
    public static final double BOTTOM_RIGHT_POCKET_Y = 710;
    // RACK (first ball in)
    public static final double RACKSTACK_X = 1192; 

}
