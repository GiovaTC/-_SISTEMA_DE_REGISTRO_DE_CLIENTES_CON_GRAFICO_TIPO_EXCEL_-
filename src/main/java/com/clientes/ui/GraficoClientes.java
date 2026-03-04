package com.clientes.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import com.clientes.service.ClienteService;

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
                    "DISTRIBUCION DE CLIENTES",
                    dataset,
                    true,
                    true,
                    false
            );

            ChartFrame frame = new ChartFrame("GRAFICO CLIENTES", chart);
            frame.setSize(600, 400);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
