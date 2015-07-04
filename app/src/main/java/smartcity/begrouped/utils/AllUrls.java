package smartcity.begrouped.utils;

/**
 * Created by a on 29/04/2015.
 */
public final class AllUrls {


    public static final String base="http://begrouped.mahdi-dif.com/index.php/";

    //---- Gestion des Authentication --------------------------------------------------------------
    public static final String authenticate_user_url=base+"login/";
    public static final String register_user_url =base+"register/";
    public static final String authenticate_me =base+"me";
    public static final String GET_USER_INFO = base + "user_controller/getuser/"; //parametres : username
    //------------------------------------------------------------------------------------------------

    //---- Gestion des groupes -----------------------------------------------------------------------
    public static final String GET_GROUP_INFORMATIONS = base + "group_controller/getgroup/"; //parametres : [groupname] (facultative)
    public static final String GET_GROUP_MEMBERS = base + "group_controller/getgroupmember/"; //paramatres : groupname 
    public static final String GET_GROUP_DEMANDES = base + "group_controller/getgroupesdemandees/"; // Aucun parametres 
    public static final String ASK_JOIN_GROUP = base + "group_controller/demandeajoutgroup/"; //paramatres : groupname
    public static final String GET_PENDING_DEMANDS = base + "group_controller/getdemandeenattente/"; //paramatres : groupname (fait par le superviseur)
    public static final String ACCEPT_MEMBER_TO_GROUP = base + "group_controller/accepterajoutgroup/"; //paramatres : groupname, username (fait par le superviseur)
    public static final String SORTIR_GROUP_USER = base + "group_controller/sortirgroupfromuser/"; //parametres : groupname
    public static final String EXPULSER_GROUP_SUPERVISOR = base + "group_controller/sortirGroupFromSupervisor/"; //parametres : groupname, username (fait par le superviseur)
    public static final String CREATE_GROUP = base + "group_controller/createGroup/"; //parametres : groupname, regionname,[expirationDate(YYYY-MM-JJ)]
    public static final String DELETE_GROUP = base + "group_controller/deleteGroup/"; //parametres : groupname (fait par le superviseur)
    public static final String GET_GROUP_ALL_INFORMATIONS = base + "group_controller/getGroupAllInfo/"; //parametres : groupname

    //------------------------------------------------------------------------------------------------

    //---- Gestion des positions ---------------------------------------------------------------------
    public static final String SEND_POSITION = base + "position_controller/sendMyPosition/"; //parametres : latitude,longitude
    public static final String GET_GROUP_POSITION = base + "position_controller/getGroupPosition/"; //parametres : groupname
    //------------------------------------------------------------------------------------------------

    //---- Gestion des poi ---------------------------------------------------------------------------
    public static final String GET_NEAREST_POI = base + "poi_controller/getNearestPoint/"; //parametres : latitude,longitude
    public static final String SEARCH_POI_BY_NAME = base + "poi_controller/searchPoint/"; //parametres : name (part of name)
    public static final String GET_POI_BY_ID = base + "poi_controller/getPoiDetail/"; //parametres : id
    //------------------------------------------------------------------------------------------------

    //---- Gestion des Rendez-vous -------------------------------------------------------------------
    public static final String CREATE_RDV=base +"rdv_controller/createRDV/"; //parametres : groupname,latitude,longitude,date (YYYY-MM-JJ),heure,minute,seconde
    public static final String GET_RDV=base+"rdv_controller/getRDV/"; // parametres : groupname
    public static final String REMOVE_RDV=base+"rdv_controller/removeRDV/"; // parametres : groupname
    //------------------------------------------------------------------------------------------------

    //---- Gestion des programmes -------------------------------------------------------------------
    public static final String ADD_LIGNE_PROGRAM=base+"program_controller/addPoiProgram/"; //parametres : poi_id,groupname,date (YYYY-MM-JJ),[heure,minute,seconde] (fait par le superviseur)
    public static final String GET_PROGRAM=base+"program_controller/getProgram/"; //parametres : groupname,[date (YYYY-MM-JJ)]
    public static final String DELETE_PROGRAM=base+"program_controller/deleteProgram/"; //parametres : groupname,[date (YYYY-MM-JJ)]
    //------------------------------------------------------------------------------------------------

}
