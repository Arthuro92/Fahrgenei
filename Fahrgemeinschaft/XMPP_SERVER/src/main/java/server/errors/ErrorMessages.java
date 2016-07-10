package server.errors;

/**
 * Created by Lennart on 10.06.2016.
 * Stores all Error Messages which can be send to Clients
 */
public class ErrorMessages {
    public static final String MYSQL_ERROR = "error: SQL Exception on Server. Vielleicht hilft manuelles Syncronisieren?";
    public static final String NO_ACCESS = "error: Access Denied!";
    public static final String INVALID_INFORMTION = "error: Information_invalid (Extras send to server not correct)";
    public static final String USER_NOT_FOUND = "error: Nutzer nicht gefunden!";
    public static final String SENDING_INVITATION_FAILED = "error: Fehler beim senden der Einladung! Versuchen Sie es erneut!";
    public static final String SEND_BROADCAST_USER_FAILED = "error: Fehler beim senden der Einladung! Versuchen Sie es erneut!";
}
