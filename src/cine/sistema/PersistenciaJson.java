package cine.sistema;

import cine.modelos.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Persistencia JSON minimalista y propia (sin librerías externas).
 * Archivo: cine_data.json en la carpeta del proyecto.
 *
 * Estructura JSON generada por esta clase:
 * {
 *   "peliculas":[{...}],
 *   "salas":[{...}],
 *   "funciones":[{...}],
 *   "clientes":[{...}],
 *   "reservas":[{...}]
 * }
 *
 * Las referencias entre objetos se hacen por id:
 * - Funcion: peliculaId, salaId, fecha, hora
 * - Reserva: codigo, clienteId, funcionId, asientos: ["A1","B2"]
 */
public class PersistenciaJson {

    private static final String ARCHIVO = "cine_data.json";

    public static void guardar(Cine cine) {
        Map<String,Object> root = new LinkedHashMap<>();
        root.put("peliculas", cine.getPeliculas().stream().map(PersistenciaJson::peliculaToMap).collect(Collectors.toList()));
        root.put("salas", cine.getSalas().stream().map(PersistenciaJson::salaToMap).collect(Collectors.toList()));
        root.put("funciones", cine.getFunciones().stream().map(PersistenciaJson::funcionToMap).collect(Collectors.toList()));
        root.put("clientes", cine.getClientes().stream().map(PersistenciaJson::clienteToMap).collect(Collectors.toList()));
        root.put("reservas", cine.getReservas().stream().map(PersistenciaJson::reservaToMap).collect(Collectors.toList()));

        String json = toJson(root);
        try (FileWriter fw = new FileWriter(ARCHIVO)) {
            fw.write(json);
            fw.flush();
            System.out.println("Datos guardados en " + ARCHIVO);
        } catch (IOException e) {
            System.err.println("Error guardando JSON: " + e.getMessage());
        }
    }

    public static Cine cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists()) {
            System.out.println("Archivo de datos no existe. Creando Cine vacío.");
            return new Cine();
        }
        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line).append("\n");
            }
            String json = sb.toString();
            Map<String, Object> root = parseJsonObject(json);

            // load peliculas
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> pelMaps = (List<Map<String,Object>>) root.getOrDefault("peliculas", Collections.emptyList());
            Map<String,Pelicula> peliculasById = new LinkedHashMap<>();
            for (Map<String,Object> pm : pelMaps) {
                String id = (String) pm.get("id");
                String titulo = (String) pm.get("titulo");
                String genero = (String) pm.get("genero");
                int dur = ((Number) pm.get("duracion")).intValue();
                String clas = (String) pm.get("clasificacion");
                Pelicula p = new Pelicula(id, titulo, genero, dur, clas);
                peliculasById.put(id, p);
            }

            // salas
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> salaMaps = (List<Map<String,Object>>) root.getOrDefault("salas", Collections.emptyList());
            Map<String,Sala> salasById = new LinkedHashMap<>();
            for (Map<String,Object> sm : salaMaps) {
                String id = (String) sm.get("id");
                int numero = ((Number) sm.get("numero")).intValue();
                int filas = ((Number) sm.get("filas")).intValue();
                int cols = ((Number) sm.get("columnas")).intValue();
                Sala s = new Sala(id, numero, filas, cols);
                salasById.put(id, s);
            }

            // funciones
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> funcMaps = (List<Map<String,Object>>) root.getOrDefault("funciones", Collections.emptyList());
            Map<String,Funcion> funcionesById = new LinkedHashMap<>();
            for (Map<String,Object> fm : funcMaps) {
                String id = (String) fm.get("id");
                String pid = (String) fm.get("peliculaId");
                String sid = (String) fm.get("salaId");
                String fechaStr = (String) fm.get("fecha");
                String horaStr = (String) fm.get("hora");
                Pelicula p = peliculasById.get(pid);
                Sala s = salasById.get(sid);
                LocalDate fecha = LocalDate.parse(fechaStr);
                LocalTime hora = LocalTime.parse(horaStr);
                Funcion fn = new Funcion(id, p, s, fecha, hora);
                funcionesById.put(id, fn);
            }

            // clientes
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> cliMaps = (List<Map<String,Object>>) root.getOrDefault("clientes", Collections.emptyList());
            Map<String,Cliente> clientesById = new LinkedHashMap<>();
            for (Map<String,Object> cm : cliMaps) {
                String id = (String) cm.get("id");
                String nombre = (String) cm.get("nombre");
                String documento = (String) cm.get("documento");
                String correo = (String) cm.get("correo");
                boolean vip = cm.getOrDefault("vip", Boolean.FALSE) == Boolean.TRUE;
                Cliente c;
                if (vip) c = new ClienteVIP(id, nombre, documento, correo, 1);
                else c = new Cliente(id, nombre, documento, correo);
                clientesById.put(id, c);
            }

            // reservas
            @SuppressWarnings("unchecked")
            List<Map<String,Object>> resMaps = (List<Map<String,Object>>) root.getOrDefault("reservas", Collections.emptyList());
            List<Reserva> reservasList = new ArrayList<>();
            for (Map<String,Object> rm : resMaps) {
                String codigo = (String) rm.get("codigo");
                String clienteId = (String) rm.get("clienteId");
                String funcionId = (String) rm.get("funcionId");
                @SuppressWarnings("unchecked")
                List<String> seats = (List<String>) rm.getOrDefault("asientos", Collections.emptyList());

                Cliente c = clientesById.get(clienteId);
                Funcion fn = funcionesById.get(funcionId);
                List<Asiento> asientos = new ArrayList<>();
                for (String scode : seats) {
                    // scode like "A1"
                    char fila = scode.charAt(0);
                    int num = Integer.parseInt(scode.substring(1));
                    Asiento a = fn.getSala().buscarAsiento(fila, num);
                    if (a != null) {
                        a.ocupar(c); // mark occupied
                        asientos.add(a);
                    }
                }
                Reserva r = new Reserva(codigo, c, fn, asientos);
                reservasList.add(r);
                // add reservation to client and function
                if (c != null) c.getReservas().add(r);
                if (fn != null) fn.getReservas().add(r);
            }

            // finally build Cine
            Cine cine = new Cine();
            cine.setPeliculas(new ArrayList<>(peliculasById.values()));
            cine.setSalas(new ArrayList<>(salasById.values()));
            cine.setFunciones(new ArrayList<>(funcionesById.values()));
            cine.setClientes(new ArrayList<>(clientesById.values()));
            cine.setReservas(reservasList);

            System.out.println("Datos cargados desde " + ARCHIVO);
            return cine;

        } catch (Exception e) {
            System.err.println("Error cargando JSON: " + e.getMessage());
            e.printStackTrace();
            return new Cine();
        }
    }

    // ----------------- helpers para convertir objetos a Map -----------------
    private static Map<String,Object> peliculaToMap(Pelicula p) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        m.put("titulo", p.getTitulo());
        m.put("genero", p.getGenero());
        m.put("duracion", p.getDuracion());
        m.put("clasificacion", p.getClasificacion());
        return m;
    }

    private static Map<String,Object> salaToMap(Sala s) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("id", s.getId());
        m.put("numero", s.getNumero());
        m.put("filas", s.getFilas());
        m.put("columnas", s.getColumnas());
        return m;
    }

    private static Map<String,Object> funcionToMap(Funcion f) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("id", f.getId());
        m.put("peliculaId", f.getPelicula().getId());
        m.put("salaId", f.getSala().getId());
        m.put("fecha", f.getFecha().toString());
        m.put("hora", f.getHora().toString());
        return m;
    }

    private static Map<String,Object> clienteToMap(Cliente c) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("id", c.getId());
        m.put("nombre", c.getNombre());
        m.put("documento", c.getDocumento());
        m.put("correo", c.getCorreo());
        m.put("vip", c instanceof ClienteVIP);
        return m;
    }

    @SuppressWarnings("unused")
    private static Map<String,Object> reservaToMap(Reserva r) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("codigo", r.getCodigo());
        m.put("clienteId", r.getCliente().getId());
        m.put("funcionId", r.getFuncion().getId());
        List<String> seats = r.getAsientos().stream().map(a -> "" + a.getFila() + a.getNumero()).collect(Collectors.toList());
        m.put("asientos", seats);
        return m;
    }

    // ----------------- JSON minimal (serializa Map/List/strings/numbers/booleans) -----------------
    private static String toJson(Object o) {
        if (o == null) return "null";
        if (o instanceof Map) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            Map<?,?> map = (Map<?,?>) o;
            boolean first = true;
            for (Map.Entry<?,?> e : map.entrySet()) {
                if (!first) sb.append(",");
                sb.append("\"").append(escape(e.getKey().toString())).append("\":").append(toJson(e.getValue()));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        } else if (o instanceof List) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            List<?> list = (List<?>) o;
            boolean first = true;
            for (Object item : list) {
                if (!first) sb.append(",");
                sb.append(toJson(item));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        } else if (o instanceof String) {
            return "\"" + escape((String)o) + "\"";
        } else if (o instanceof Number || o instanceof Boolean) {
            return o.toString();
        } else {
            return "\"" + escape(o.toString()) + "\"";
        }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n","\\n").replace("\r","\\r");
    }

    // ----------------- JSON parser VERY simple: only supports the format produced above -----------------
    private static Map<String,Object> parseJsonObject(String json) throws Exception {
        json = json.trim();
        if (!json.startsWith("{")) throw new Exception("JSON no empieza con {");
        // Very small recursive parser using tokenizer
        JsonTokenizer tok = new JsonTokenizer(json);
        Object obj = parseValue(tok);
        if (!(obj instanceof Map)) throw new Exception("Root no es objeto");
        @SuppressWarnings("unchecked")
        Map<String,Object> result = (Map<String,Object>) obj;
        return result;
    }

    private static Object parseValue(JsonTokenizer tok) throws Exception {
        JsonToken t = tok.peek();
        if (t == null) return null;
        switch (t.type) {
            case LEFT_BRACE:
                return parseObject(tok);
            case LEFT_BRACKET:
                return parseArray(tok);
            case STRING:
                return tok.next().text;
            case NUMBER:
                String num = tok.next().text;
                if (num.contains(".") || num.contains("e") || num.contains("E")) return Double.parseDouble(num);
                else return Integer.parseInt(num);
            case TRUE:
                tok.next(); return Boolean.TRUE;
            case FALSE:
                tok.next(); return Boolean.FALSE;
            case NULL:
                tok.next(); return null;
            default:
                throw new Exception("Token inesperado: " + t.type);
        }
    }

    private static Map<String,Object> parseObject(JsonTokenizer tok) throws Exception {
        Map<String,Object> map = new LinkedHashMap<>();
        tok.expect(JsonTokenType.LEFT_BRACE);
        if (tok.peek().type == JsonTokenType.RIGHT_BRACE) { tok.next(); return map; }
        while (true) {
            JsonToken key = tok.next();
            if (key.type != JsonTokenType.STRING) throw new Exception("Clave no es string");
            tok.expect(JsonTokenType.COLON);
            Object val = parseValue(tok);
            map.put(key.text, val);
            JsonToken commaOrEnd = tok.next();
            if (commaOrEnd.type == JsonTokenType.RIGHT_BRACE) break;
            if (commaOrEnd.type != JsonTokenType.COMMA) throw new Exception("Esperaba , o }");
        }
        return map;
    }

    private static List<Object> parseArray(JsonTokenizer tok) throws Exception {
        List<Object> list = new ArrayList<>();
        tok.expect(JsonTokenType.LEFT_BRACKET);
        if (tok.peek().type == JsonTokenType.RIGHT_BRACKET) { tok.next(); return list; }
        while (true) {
            Object val = parseValue(tok);
            list.add(val);
            JsonToken commaOrEnd = tok.next();
            if (commaOrEnd.type == JsonTokenType.RIGHT_BRACKET) break;
            if (commaOrEnd.type != JsonTokenType.COMMA) throw new Exception("Esperaba , o ]");
        }
        return list;
    }

    // ----------------- tokenizer inner classes -----------------
    private enum JsonTokenType { LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET, COLON, COMMA, STRING, NUMBER, TRUE, FALSE, NULL, EOF }
    private static class JsonToken {
        JsonTokenType type;
        String text;
        JsonToken(JsonTokenType type, String text){ this.type = type; this.text = text; }
    }

    private static class JsonTokenizer {
        private final String s;
        private int i = 0;
        private JsonToken pushed = null;
        JsonTokenizer(String s){ this.s = s; }

        JsonToken next() throws Exception {
            if (pushed != null) { JsonToken t = pushed; pushed = null; return t; }
            skipWhitespace();
            if (i >= s.length()) return new JsonToken(JsonTokenType.EOF, null);
            char c = s.charAt(i);
            switch (c) {
                case '{': i++; return new JsonToken(JsonTokenType.LEFT_BRACE, "{");
                case '}': i++; return new JsonToken(JsonTokenType.RIGHT_BRACE, "}");
                case '[': i++; return new JsonToken(JsonTokenType.LEFT_BRACKET, "[");
                case ']': i++; return new JsonToken(JsonTokenType.RIGHT_BRACKET, "]");
                case ':': i++; return new JsonToken(JsonTokenType.COLON, ":");
                case ',': i++; return new JsonToken(JsonTokenType.COMMA, ",");
                case '"': return readString();
                default:
                    if ((c>='0' && c<='9') || c=='-') return readNumber();
                    if (s.startsWith("true", i)) { i+=4; return new JsonToken(JsonTokenType.TRUE, "true"); }
                    if (s.startsWith("false", i)) { i+=5; return new JsonToken(JsonTokenType.FALSE, "false"); }
                    if (s.startsWith("null", i)) { i+=4; return new JsonToken(JsonTokenType.NULL, "null"); }
                    throw new Exception("Caracter inesperado: " + c + " en pos " + i);
            }
        }

        JsonToken peek() throws Exception {
            if (pushed == null) pushed = next();
            return pushed;
        }

        void expect(JsonTokenType type) throws Exception {
            JsonToken t = next();
            if (t.type != type) throw new Exception("Esperaba " + type + " pero vino " + t.type);
        }

        private void skipWhitespace() {
            while (i < s.length()) {
                char c = s.charAt(i);
                if (c==' '||c=='\n'||c=='\r'||c=='\t') i++; else break;
            }
        }

        private JsonToken readString() throws Exception {
            if (s.charAt(i) != '"') throw new Exception("No empieza string");
            i++;
            StringBuilder sb = new StringBuilder();
            while (i < s.length()) {
                char c = s.charAt(i++);
                if (c == '"') break;
                if (c == '\\') {
                    if (i >= s.length()) break;
                    char esc = s.charAt(i++);
                    switch (esc) {
                        case '"': sb.append('"'); break;
                        case '\\': sb.append('\\'); break;
                        case '/': sb.append('/'); break;
                        case 'n': sb.append('\n'); break;
                        case 'r': sb.append('\r'); break;
                        case 't': sb.append('\t'); break;
                        case 'b': sb.append('\b'); break;
                        case 'f': sb.append('\f'); break;
                        default: sb.append(esc); break;
                    }
                } else sb.append(c);
            }
            return new JsonToken(JsonTokenType.STRING, sb.toString());
        }

        private JsonToken readNumber() {
            int start = i;
            char c = s.charAt(i);
            if (c=='-') i++;
            while (i < s.length()) {
                char d = s.charAt(i);
                if ((d>='0' && d<='9')||d=='.'||d=='e'||d=='E'||d=='+'||d=='-') i++;
                else break;
            }
            String num = s.substring(start, i);
            return new JsonToken(JsonTokenType.NUMBER, num);
        }
    }
}


