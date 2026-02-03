// package com.daiict.Handler;

// import java.io.File;
// import java.io.IOException;
// import java.io.OutputStream;
// import java.net.URI;
// import java.net.URLDecoder;
// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.sql.SQLException;
// import java.util.List;
// import java.util.stream.Collectors;

// import com.daiict.DAO.CulturalCommitteeMemberDAO;
// import com.daiict.DAO.EventDAO;
// import com.daiict.DAO.OrganizerDAO;
// import com.daiict.DAO.ParticipantDAO;
// // import com.daiict.DAO.VendorDAO;
// import com.daiict.DAO.VenueDAO;
// import com.daiict.Model.CulturalCommitteeMember;
// import com.daiict.Model.Event;
// import com.daiict.Model.Organizer;
// import com.daiict.Model.Participant;
// // import com.daiict.Model.Organizer;
// // import com.daiict.Model.Vendor;
// import com.daiict.Model.Venue;
// import com.sun.net.httpserver.HttpExchange;
// import com.sun.net.httpserver.HttpHandler;

// public class WebAppHandler implements HttpHandler {

//     // Path to the HTML template file relative to the project root
//     private static final String HTML_TEMPLATE_PATH = "frontend/index.html"; 
    
//     // DAO instances for accessing the database
//     private final EventDAO eventDAO = new EventDAO();
//     private final CulturalCommitteeMemberDAO culturalCommitteeMemberDAO = new CulturalCommitteeMemberDAO();
//     private final OrganizerDAO organizerDAO = new OrganizerDAO();
//     // private final VendorDAO vendorDAO = new VendorDAO(); // Instantiating the new DAO
//     private final VenueDAO venueDAO = new VenueDAO();   // Instantiating the new DAO
//     private final ParticipantDAO participantDAO = new ParticipantDAO();   // Instantiating the new DAO

//     private void handleParticipantApi(HttpExchange exchange) throws IOException {
//         URI requestUri = exchange.getRequestURI();
//         String query = requestUri.getQuery();
//         String eventName = null;
        
//         // Extract EventName from query string: ?event=...
//         if (query != null) {
//             for (String param : query.split("&")) {
//                 if (param.startsWith("event=")) {
//                     // Use URLDecoder to handle spaces/special characters in event names
//                     eventName = URLDecoder.decode(param.substring(6), StandardCharsets.UTF_8); 
//                     break;
//                 }
//             }
//         }
        
//         if (eventName == null || eventName.trim().isEmpty()) {
//             sendResponse(exchange, 400, "{\"error\": \"Event parameter is required.\"}", "application/json");
//             return;
//         }
        
//         String jsonResponse;
        
//         try {
//             List<Participant> participants = participantDAO.getParticipantsByEvent(eventName);

//             // Manual JSON Construction: Build the array string
//             StringBuilder finalJsonArray = new StringBuilder();
//             finalJsonArray.append("[");
//             for (int i = 0; i < participants.size(); i++) {
//                 finalJsonArray.append(participants.get(i).toJson()); 
//                 if (i < participants.size() - 1) {
//                     finalJsonArray.append(", ");
//                 }
//             }
//             finalJsonArray.append("]");
//             jsonResponse = finalJsonArray.toString();

//             // Success: Send JSON
//             sendResponse(exchange, 200, jsonResponse, "application/json");

//         } catch (Exception e) {
//             // CRITICAL: Ensure a JSON response is sent even on error, resolving the SyntaxError
//             System.err.println("Participant API Error for event " + eventName + ": " + e.getMessage());
//             e.printStackTrace();
//             sendResponse(exchange, 500, "{\"error\": \"Internal server processing failed in DAO.\"}", "application/json");
//         }
//     }

//     @Override
//     public void handle(HttpExchange exchange) throws IOException {
//         String response = "";
//         int statusCode = 200;

//         String path = exchange.getRequestURI().getPath(); // <-- Definition of 'path'
//         String method = exchange.getRequestMethod();


//         if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
//             try {
//                 if (path.equals("/api/participants")) {
//                     handleParticipantApi(exchange); // <-- This is the actual call
//                     return;
//                 }
//                 // 1. Get ALL Data from DAOs
//                 // List<Event> events = eventDAO.getAllEvents();
//                 List<Event> events = eventDAO.getAllUpcomingEvents();
//                 List<CulturalCommitteeMember> members = culturalCommitteeMemberDAO.getActiveMembers();
//                 List<Organizer> organizers = organizerDAO.getAllOrganizers();
//                 // List<Vendor> vendors = vendorDAO.getAllVendors();   // New data fetch
//                 List<Venue> venues = venueDAO.getAllVenues();   

//                 // 2. Convert Data Lists to JSON Strings
//                 // The stream().map(Model::toJson).collect(Collectors.joining(",")) creates a
//                 // comma-separated list of JSON objects, which is then wrapped in [].
//                 String eventsJson = "[" + events.stream()
//                                     .map(Event::toJson) 
//                                     .collect(Collectors.joining(",")) + "]";
                                    
//                 String membersJson = "[" + members.stream()
//                                      .map(CulturalCommitteeMember::toJson) 
//                                      .collect(Collectors.joining(",")) + "]";

//                 String organizersJson = "[" + organizers.stream()
//                                      .map(Organizer::toJson) 
//                                      .collect(Collectors.joining(",")) + "]";

//                 // String vendorsJson = "[" + vendors.stream()
//                 //                      .map(Vendor::toJson) 
//                 //                      .collect(Collectors.joining(",")) + "]";

//                 String venuesJson = "[" + venues.stream()
//                                      .map(Venue::toJson) 
//                                      .collect(Collectors.joining(",")) + "]";

//                 // 3. Load View Template (HTML)
//                 File htmlFile = new File(HTML_TEMPLATE_PATH);
                
//                 // Check if the HTML template exists
//                 if (!htmlFile.exists()) {
//                     throw new IOException("HTML template file not found: " + HTML_TEMPLATE_PATH);
//                 }
//                 String template = new String(Files.readAllBytes(htmlFile.toPath()));

//                 // 4. Inject JSON Data into Placeholders in the HTML template
//                 response = template.replace("{{EVENTS_DATA}}", eventsJson)
//                                    .replace("{{MEMBERS_DATA}}", membersJson)
//                                    .replace("{{ORGANIZERS_DATA}}", organizersJson)
//                                 //    .replace("{{VENDORS_DATA}}", vendorsJson)
//                                    .replace("{{VENUES_DATA}}", venuesJson);
                
//             } catch (SQLException e) {
//                 // Handle database connection or query errors
//                 System.err.println("Database Error: " + e.getMessage());
//                 e.printStackTrace();
//                 statusCode = 500; 
//                 response = "<h1>❌ Database Error</h1><p>Could not load data. Check server logs for details.</p>";
//             } catch (IOException e) {
//                  // Handle file reading errors
//                  System.err.println("File I/O Error: " + e.getMessage());
//                  e.printStackTrace();
//                  statusCode = 500;
//                  response = "<h1>Server Error</h1><p>Could not load HTML template. Path: " + HTML_TEMPLATE_PATH + "</p>";
//             } catch (Exception e) {
//                  // Catch any unexpected runtime exceptions
//                  System.err.println("Unexpected Error: " + e.getMessage());
//                  e.printStackTrace();
//                  statusCode = 500;
//                  response = "<h1>Internal Server Error</h1><p>An unexpected error occurred.</p>";
//             }
//         } else {
//             // Handle non-GET requests
//             statusCode = 405; // Method Not Allowed
//             response = "<h1>Method Not Allowed</h1>";
//         }

//         // Send the response
//         exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
//         // Ensure content length is correctly calculated using response.getBytes().length
//         exchange.sendResponseHeaders(statusCode, response.getBytes().length);
//         OutputStream os = exchange.getResponseBody();
//         os.write(response.getBytes());
//         os.close();
//     }

//     private void sendResponse(HttpExchange exchange, int statusCode, String responseBody, String contentType) throws IOException {
//         exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
//         byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
//         exchange.sendResponseHeaders(statusCode, responseBytes.length);
//         try (OutputStream os = exchange.getResponseBody()) {
//             os.write(responseBytes);
//         }
//     }
// }


package com.daiict.Handler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.daiict.DAO.CulturalCommitteeMemberDAO;
import com.daiict.DAO.EventDAO;
import com.daiict.DAO.OrganizerDAO;
import com.daiict.DAO.ParticipantDAO;
import com.daiict.DAO.VenueDAO;
import com.daiict.Model.CulturalCommitteeMember;
import com.daiict.Model.Event;
import com.daiict.Model.Organizer;
import com.daiict.Model.Participant;
import com.daiict.Model.Venue;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebAppHandler implements HttpHandler {

    private static final String HTML_TEMPLATE_PATH = "frontend/index.html"; 
    
    // DAO instances
    private final EventDAO eventDAO = new EventDAO();
    private final CulturalCommitteeMemberDAO culturalCommitteeMemberDAO = new CulturalCommitteeMemberDAO();
    private final OrganizerDAO organizerDAO = new OrganizerDAO();
    private final VenueDAO venueDAO = new VenueDAO(); 
    private final ParticipantDAO participantDAO = new ParticipantDAO(); 

    // --- Helper for sending response (kept outside handleParticipantApi) ---
    private void sendResponse(HttpExchange exchange, int statusCode, String responseBody, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
        byte[] responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    // --- Simplified handleParticipantApi logic (now called from handle) ---
    private void handleParticipantApi(HttpExchange exchange) throws IOException {
        URI requestUri = exchange.getRequestURI();
        String query = requestUri.getQuery();
        String eventName = null;
        
        // 1. Extract EventName from query string: ?event=...
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("event=")) {
                    eventName = URLDecoder.decode(param.substring(6), StandardCharsets.UTF_8); 
                    break;
                }
            }
        }
        
        // 2. Validation
        if (eventName == null || eventName.trim().isEmpty()) {
            sendResponse(exchange, 400, "{\"error\": \"Event parameter is required.\"}", "application/json");
            return;
        }
        
        String jsonResponse;
        
        try {
            List<Participant> participants = participantDAO.getParticipantsByEvent(eventName);

            // 3. Manual JSON Construction
            StringBuilder finalJsonArray = new StringBuilder();
            finalJsonArray.append("[");
            for (int i = 0; i < participants.size(); i++) {
                finalJsonArray.append(participants.get(i).toJson()); 
                if (i < participants.size() - 1) {
                    finalJsonArray.append(", ");
                }
            }
            finalJsonArray.append("]");
            jsonResponse = finalJsonArray.toString();

            // 4. Success: Send JSON
            sendResponse(exchange, 200, jsonResponse, "application/json");

        } catch (Exception e) {
            // Error handling logic (sends JSON error)
            System.err.println("Participant API Error for event " + eventName + ": " + e.getMessage());
            e.printStackTrace();
            sendResponse(exchange, 500, "{\"error\": \"Internal server processing failed in DAO.\"}", "application/json");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;

        // --- FIX: Declare path and method at the top of the handle method ---
        String path = exchange.getRequestURI().getPath(); 
        String method = exchange.getRequestMethod();
        // --- END FIX ---


        if ("GET".equalsIgnoreCase(method)) {
            
            // --- NEW ROUTING LOGIC ---
            if (path.equals("/api/participants")) {
                handleParticipantApi(exchange); 
                return; // CRITICAL: EXIT after API call
            }
            // --- END ROUTING LOGIC ---


            try {
                // FALLBACK: If not the API path, proceed with main HTML page logic

                // 1. Get ALL Data from DAOs
                List<Event> events = eventDAO.getAllUpcomingEvents();
                List<CulturalCommitteeMember> members = culturalCommitteeMemberDAO.getActiveMembers();
                List<Organizer> organizers = organizerDAO.getAllOrganizers();
                List<Venue> venues = venueDAO.getAllVenues(); 

                // 2. Convert Data Lists to JSON Strings
                String eventsJson = "[" + events.stream().map(Event::toJson).collect(Collectors.joining(",")) + "]";
                String membersJson = "[" + members.stream().map(CulturalCommitteeMember::toJson).collect(Collectors.joining(",")) + "]";
                String organizersJson = "[" + organizers.stream().map(Organizer::toJson).collect(Collectors.joining(",")) + "]";
                String venuesJson = "[" + venues.stream().map(Venue::toJson).collect(Collectors.joining(",")) + "]";

                // 3. Load View Template (HTML)
                File htmlFile = new File(HTML_TEMPLATE_PATH);
                if (!htmlFile.exists()) {
                    throw new IOException("HTML template file not found: " + HTML_TEMPLATE_PATH);
                }
                String template = new String(Files.readAllBytes(htmlFile.toPath()));

                // 4. Inject JSON Data into Placeholders in the HTML template
                response = template.replace("{{EVENTS_DATA}}", eventsJson)
                                   .replace("{{MEMBERS_DATA}}", membersJson)
                                   .replace("{{ORGANIZERS_DATA}}", organizersJson)
                                   .replace("{{VENUES_DATA}}", venuesJson);
                
            } catch (SQLException e) {
                System.err.println("Database Error: " + e.getMessage());
                e.printStackTrace();
                statusCode = 500; 
                response = "<h1>❌ Database Error</h1><p>Could not load data. Check server logs for details.</p>";
            } catch (IOException e) {
                System.err.println("File I/O Error: " + e.getMessage());
                e.printStackTrace();
                statusCode = 500;
                response = "<h1>Server Error</h1><p>Could not load HTML template. Path: " + HTML_TEMPLATE_PATH + "</p>";
            } catch (Exception e) {
                System.err.println("Unexpected Error: " + e.getMessage());
                e.printStackTrace();
                statusCode = 500;
                response = "<h1>Internal Server Error</h1><p>An unexpected error occurred.</p>";
            }
        } 
        else if ("POST".equalsIgnoreCase(method)) {
            // Route POST requests
            if (path.equals("/editEvent")) { // Check for the submission path
                handleEventUpdate(exchange);
                return;
            }
            if (path.equals("/deleteEvent")) { 
                handleEventDelete(exchange);
                return;
            }
            if (path.equals("/addEvent")) {
                // This is the INSERT path
                handleEventAdd(exchange); // <<< NEW ROUTE
                return;
            }
        }else {
            // Handle non-GET requests
            statusCode = 405; // Method Not Allowed
            response = "<h1>Method Not Allowed</h1>";
        }

        // Send the final HTML response (used only by the main page logic)
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private Map<String, String> parseFormBody(String body) {
        Map<String, String> result = new HashMap<>();
        if (body == null || body.isEmpty()) return result;
        
        for (String param : body.split("&")) {
            String[] pair = param.split("=");
            if (pair.length >= 1) {
                try {
                    String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8.toString());
                    // Handle cases where the value might be empty or missing
                    String value = (pair.length == 2) ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8.toString()) : "";
                    result.put(key, value);
                } catch (Exception e) {
                    System.err.println("Error parsing form param: " + param);
                }
            }
        }
        return result;
    }

    private void handleEventUpdate(HttpExchange exchange) throws IOException {
        // 1. Read the POST body
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        
        // 2. Parse the form data
        Map<String, String> formData = parseFormBody(requestBody);
        
        // 3. Extract and map data to Event model
        String eventName = formData.get("eventName"); 
        String startDateStr = formData.get("eventStartDate");
        String endDateStr = formData.get("eventEndDate");
        String eventDesc = formData.get("eventDesc");
        String venueName = formData.get("venueName");
        String status = formData.get("status");
        String originalEventName = formData.get("originalEventName");

        if (originalEventName == null || originalEventName.isEmpty()) {
            // This happens if the hidden input's 'name' attribute is missing in HTML.
            sendResponse(exchange, 400, "Error: Original Event Name (PK) missing.", "text/plain");
            return;
        }

        // Basic validation
        if (eventName == null || startDateStr == null || status == null || endDateStr == null || eventDesc == null || venueName == null) {
            sendResponse(exchange, 400, "Missing required fields (eventName, startDate, status).", "text/plain");
            return;
        }

        try {
            Date startDate = Date.valueOf(startDateStr);
            // endDate might be null, handle separately
            Date endDate = (endDateStr != null && !endDateStr.isEmpty()) ? Date.valueOf(endDateStr) : null;
            
            Event event = new Event();
            event.setEventName(eventName); // This acts as the PK for the WHERE clause
            event.setStartDate(startDate);
            event.setEndDate(endDate);
            event.setEventDesc(eventDesc);
            event.setVenueName(venueName);
            event.setStatus(status);
            
            // 4. Call DAO to execute the update
            boolean success = eventDAO.updateEvent(event, originalEventName);
            
            // 5. Send Response (Redirect on success)
            if (success) {
                // Success: Send a 303 Redirect to force the browser to reload the main page
                exchange.getResponseHeaders().set("Location", "/"); 
                exchange.sendResponseHeaders(303, -1);
            } else {
                sendResponse(exchange, 500, "Database update failed (0 rows affected).", "text/plain");
            }

        } catch (IllegalArgumentException e) {
            // Catches Date.valueOf errors if date format is wrong
            sendResponse(exchange, 400, "Invalid date format. Use YYYY-MM-DD.", "text/plain");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error during update.", "text/plain");
        }
    }

    private void handleEventDelete(HttpExchange exchange) throws IOException {
        // Read the POST body (which contains the eventName)
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> formData = parseFormBody(requestBody);
        
        // The EventName is passed as the 'eventName' field in the form data
        String eventName = formData.get("eventName"); 

        if (eventName == null || eventName.trim().isEmpty()) {
            sendResponse(exchange, 400, "Missing EventName parameter.", "text/plain");
            return;
        }

        try {
            // Call DAO to execute the delete
            boolean success = eventDAO.deleteEvent(eventName);
            
            // Redirect regardless of success to reload the page and reflect changes/errors
            if (success) {
                exchange.getResponseHeaders().set("Location", "/"); 
                exchange.sendResponseHeaders(303, -1);
            } else {
                // Redirect even on failure, but show a console error for debugging
                System.err.println("Database delete failed: Event '" + eventName + "' not found or delete constraint violated.");
                exchange.getResponseHeaders().set("Location", "/"); 
                exchange.sendResponseHeaders(303, -1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error during delete.", "text/plain");
        }
    }

    private void handleEventAdd(HttpExchange exchange) throws IOException {
        // 1. Read the POST body
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        
        // 2. Parse the form data (assuming you use the provided parseFormBody utility)
        Map<String, String> formData = parseFormBody(requestBody);
        
        // 3. Extract and map data to Event model
        String eventName = formData.get("eventName"); 
        String startDateStr = formData.get("eventStartDate");
        String endDateStr = formData.get("eventEndDate");
        String eventDesc = formData.get("eventDesc");
        String venueName = formData.get("venueName");
        String status = formData.get("status"); // Expected to be 'Planning' or 'Upcoming' in Add Mode

        // Basic validation
        if (eventName == null || startDateStr == null || status == null) {
            sendResponse(exchange, 400, "Missing required fields for Add Event.", "text/plain");
            return;
        }

        try {
            // Convert string dates to java.sql.Date
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = (endDateStr != null && !endDateStr.isEmpty()) ? Date.valueOf(endDateStr) : null;
            
            Event event = new Event();
            event.setEventName(eventName);
            event.setStartDate(startDate);
            event.setEndDate(endDate);
            event.setEventDesc(eventDesc);
            event.setVenueName(venueName);
            event.setStatus(status);
            
            // 4. Call DAO to execute the INSERT
            boolean success = eventDAO.addEvent(event); 
            
            // 5. Send Response (Redirect on success)
            if (success) {
                exchange.getResponseHeaders().set("Location", "/"); 
                exchange.sendResponseHeaders(303, -1); // Redirect back to the main page
            } else {
                sendResponse(exchange, 500, "Database insert failed (Event Name may already exist).", "text/plain");
            }

        } catch (IllegalArgumentException e) {
            // Catches errors if date format is wrong
            sendResponse(exchange, 400, "Invalid date format. Use YYYY-MM-DD.", "text/plain");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error during insert.", "text/plain");
        }
    }
}