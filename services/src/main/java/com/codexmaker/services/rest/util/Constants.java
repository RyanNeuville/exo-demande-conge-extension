package com.codexmaker.services.rest.util;

public class Constants {
    public static final String DEMANDES_FILE_PATH = "/com/codexmaker/services/rest/data/demandes.json";
    public static final String DATE_FORMAT_PATERN = "yyyy-MM-dd";

    /** End point */
    public static final String MAIN_END_POINT = "/conges";
    public static final String SUBMIT_END_POINT = "/submit";
    public static final String ALL_DEMANDES_END_POINT = "/all";
    public static final String MY_DEMANDE_END_POINT = "/my";
    public static final String MY_RELATIONS_DEMANDE_END_POINT = "/relations";
    public static final String DEMANDE_EN_ATTANTE_END_POINT = "/enattente";
    public static final String UPDATE_DEMANDE_END_POINT = "/update";
    public static final String APPROUVER_DEMANDE_END_POINT = "/approve";
    public static final String REJETER_DEMANDE_END_POINT = "/reject";
    public static final String CANCEL_DEMANDE_END_POINT = "/cancel";

    /** Messages d'erreurs */
    public static final String CHECKING_ROLE_ERROR = "Error checking admin role";


    /** Messagess de logs */
    public static final String LOG_INFO_DEMANDE_FILE_CREATED = "Created new demandes.json file at {}";
    public static final String LOG_INFO_DEMANDE_FILE_CREATED_SUCCESS = "Successfully wrote to demandes.json";
    public static final String LOG_ERROR_CREATED_DEMANDE = "Error writing to demandes.json at {}";
    public static final String LOG_ERROR_READING_FILE = "Error reading demandes.json";

    /** variables */
    public static final String STATUT_EN_ATTENTE = "EN_ATTENTE";
    public static final String STATUT_REJETEE = "REJETEE";
    public static final String STATUT_APPROUVEE = "APPROUVEE";
}
