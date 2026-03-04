# -_SISTEMA_DE_REGISTRO_DE_CLIENTES_CON_GRAFICO_TIPO_EXCEL_- :.
📊 SISTEMA DE REGISTRO DE CLIENTES CON GRÁFICO TIPO EXCEL:

Java SE + Swing + Oracle 19c + JFreeChart:

<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/7ab196a8-eb0a-4c45-a93c-a56ad1615856" />      

```
Proyecto académico/profesional desarrollado en Java (IntelliJ) con:

✔ Interfaz gráfica (Swing)
✔ Formulario de registro de clientes
✔ Persistencia en Oracle 19c
✔ Procesamiento estadístico
✔ Gráfico tipo Excel (JFreeChart – gráfico de pastel con porcentajes)

Arquitectura utilizada: Modelo – DAO – Servicio – UI

🗄️ 1️⃣ Script Base de Datos – Oracle 19c
CREATE TABLE CLIENTE (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NOMBRE VARCHAR2(100),
    EDAD NUMBER,
    CIUDAD VARCHAR2(100),
    TIPO_CLIENTE VARCHAR2(50), -- Regular, Premium, VIP
    FECHA_REGISTRO DATE DEFAULT SYSDATE
);

📌 Datos de ejemplo
INSERT INTO CLIENTE (NOMBRE, EDAD, CIUDAD, TIPO_CLIENTE) 
VALUES ('Carlos', 25, 'Bogotá', 'Regular');

INSERT INTO CLIENTE (NOMBRE, EDAD, CIUDAD, TIPO_CLIENTE) 
VALUES ('Ana', 30, 'Medellín', 'Premium');

INSERT INTO CLIENTE (NOMBRE, EDAD, CIUDAD, TIPO_CLIENTE) 
VALUES ('Luis', 40, 'Cali', 'VIP');

COMMIT;

📦 2️⃣ Dependencias Maven (pom.xml)
<dependencies>

    <!-- Oracle JDBC -->
    <dependency>
        <groupId>com.oracle.database.jdbc</groupId>
        <artifactId>ojdbc11</artifactId>
        <version>23.3.0.23.09</version>
    </dependency>

    <!-- JFreeChart -->
    <dependency>
        <groupId>org.jfree</groupId>
        <artifactId>jfreechart</artifactId>
        <version>1.5.4</version>
    </dependency>

</dependencies>

🧱 3️⃣ Arquitectura del Proyecto
com.clientes
│
├── model
│     Cliente.java
│
├── dao
│     ConexionBD.java
│     ClienteDAO.java
│
├── service
│     ClienteService.java
│
└── ui
      ClienteForm.java
      GraficoClientes.java
      Main.java

🧠 4️⃣ CAPA MODELO
Cliente.java
package model;

public class Cliente {

    private int id;
    private String nombre;
    private int edad;
    private String ciudad;
    private String tipoCliente;

    public Cliente(String nombre, int edad, String ciudad, String tipoCliente) {
        this.nombre = nombre;
        this.edad = edad;
        this.ciudad = ciudad;
        this.tipoCliente = tipoCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getCiudad() {
        return ciudad;
    }
}

🔌 5️⃣ CAPA DAO
ConexionBD.java
package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "SYSTEM";
    private static final String PASS = "1234";

    public static Connection getConexion() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

ClienteDAO.java
package dao;

import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ClienteDAO {

    public void guardarCliente(Cliente cliente) throws Exception {

        String sql = "INSERT INTO CLIENTE (NOMBRE, EDAD, CIUDAD, TIPO_CLIENTE) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setInt(2, cliente.getEdad());
            ps.setString(3, cliente.getCiudad());
            ps.setString(4, cliente.getTipoCliente());

            ps.executeUpdate();
        }
    }

    public Map<String, Integer> obtenerEstadisticas() throws Exception {

        String sql = """
            SELECT TIPO_CLIENTE, COUNT(*) TOTAL
            FROM CLIENTE
            GROUP BY TIPO_CLIENTE
        """;

        Map<String, Integer> datos = new HashMap<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                datos.put(rs.getString("TIPO_CLIENTE"), rs.getInt("TOTAL"));
            }
        }

        return datos;
    }
}

⚙️ 6️⃣ CAPA SERVICE
ClienteService.java
package service;

import dao.ClienteDAO;
import model.Cliente;

import java.util.Map;

public class ClienteService {

    private ClienteDAO dao = new ClienteDAO();

    public void registrarCliente(Cliente cliente) throws Exception {
        dao.guardarCliente(cliente);
    }

    public Map<String, Integer> obtenerEstadisticas() throws Exception {
        return dao.obtenerEstadisticas();
    }
}

🖥️ 7️⃣ INTERFAZ GRÁFICA
📝 ClienteForm.java
package ui;

import model.Cliente;
import service.ClienteService;

import javax.swing.*;
import java.awt.*;

public class ClienteForm extends JFrame {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtEdad = new JTextField(5);
    private JTextField txtCiudad = new JTextField(10);
    private JComboBox<String> comboTipo =
            new JComboBox<>(new String[]{"Regular", "Premium", "VIP"});

    private ClienteService service = new ClienteService();

    public ClienteForm() {

        setTitle("Registro de Clientes");
        setSize(400, 250);
        setLayout(new GridLayout(6,2));

        add(new JLabel("Nombre:"));
        add(txtNombre);

        add(new JLabel("Edad:"));
        add(txtEdad);

        add(new JLabel("Ciudad:"));
        add(txtCiudad);

        add(new JLabel("Tipo Cliente:"));
        add(comboTipo);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnGrafico = new JButton("Ver Gráfico");

        add(btnGuardar);
        add(btnGrafico);

        btnGuardar.addActionListener(e -> guardar());
        btnGrafico.addActionListener(e -> new GraficoClientes());

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void guardar() {
        try {
            Cliente cliente = new Cliente(
                    txtNombre.getText(),
                    Integer.parseInt(txtEdad.getText()),
                    txtCiudad.getText(),
                    comboTipo.getSelectedItem().toString()
            );

            service.registrarCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente registrado");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

📈 8️⃣ GRÁFICO TIPO EXCEL (Pastel con porcentaje)
GraficoClientes.java
package ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import service.ClienteService;

import java.util.Map;

public class GraficoClientes {

    public GraficoClientes() {

        try {

            ClienteService service = new ClienteService();
            Map<String, Integer> datos = service.obtenerEstadisticas();

            DefaultPieDataset dataset = new DefaultPieDataset();

            for (String tipo : datos.keySet()) {
                dataset.setValue(tipo, datos.get(tipo));
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Distribución de Clientes",
                    dataset,
                    true,
                    true,
                    false
            );

            ChartFrame frame = new ChartFrame("Gráfico Clientes", chart);
            frame.setSize(600, 400);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

🚀 9️⃣ Clase Principal
Main.java
package ui;

public class Main {

    public static void main(String[] args) {
        new ClienteForm();
    }
}
```

📊 Resultado Visual Esperado:
## 📝 Formulario de Registro

| Vista 1 | Vista 2 | Vista 3 |
|----------|----------|----------|
| <img src="https://github.com/user-attachments/assets/6ca1c200-67eb-4722-9761-368c08b03970" width="300"/> | <img src="https://github.com/user-attachments/assets/a5df33ff-3d71-4852-b85e-6b065d5cca7d" width="300"/> | <img src="https://github.com/user-attachments/assets/1671d0af-cd8c-47b5-9bea-3c54cc4b40a0" width="300"/> | 

## 📈 Gráfico tipo Excel (Pastel con porcentajes)

| Gráfico 1 | Gráfico 2 | Gráfico 3 |
|------------|------------|------------|
| <img src="https://github.com/user-attachments/assets/1554f3bd-e758-4000-b094-f67f0eebadd4" width="300"/> | <img src="https://github.com/user-attachments/assets/1c7099f4-85bc-47c9-8752-551c88646839" width="300"/> | <img src="https://github.com/user-attachments/assets/818edd8e-bb5a-4c4b-9181-0b648edfd652" width="300"/> |    

````
📌 Procesamiento realizado

✔ Inserta clientes en Oracle 19c
✔ Agrupa por tipo
✔ Calcula totales
✔ Genera representación gráfica porcentual

🔎 Consulta SQL clave
SELECT TIPO_CLIENTE, COUNT(*) 
FROM CLIENTE
GROUP BY TIPO_CLIENTE; / .
