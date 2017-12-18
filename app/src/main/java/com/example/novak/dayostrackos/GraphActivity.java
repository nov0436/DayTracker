package com.example.novak.dayostrackos;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import java.util.ArrayList;


public class GraphActivity extends AppCompatActivity {

    private Database db;
    private ArrayList<String> categories;
    private ArrayList<DataPoint> columns;
    private ListView lv;

    ArrayList<String> listOfCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        int numberOfOccurrences;
        this.db = new Database(this);
        String currentCategory;
        categories = new ArrayList<>();
        columns = new ArrayList<>();

        listOfCategories = new ArrayList<>();

        for (int i = 0; i < getResources().getStringArray(R.array.category_array).length; i++ )
        {
            currentCategory = getResources().getStringArray(R.array.category_array)[i];

            categories.add(currentCategory);

            numberOfOccurrences = db.selectNumberOfOccurenciesOfCategory(currentCategory);

            if (numberOfOccurrences != 0)
            {
                columns.add(new DataPoint(i, numberOfOccurrences));

                String categoryWithNumber = currentCategory + ": " + numberOfOccurrences;
                listOfCategories.add(categoryWithNumber);
            }
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series;
        DataPoint[] arrayOfDataPoints = new DataPoint[columns.size()];

        for (int i = 0; i < columns.size(); i++)
        {
            arrayOfDataPoints[i] = columns.get(i);
        }

        series = new BarGraphSeries<>(arrayOfDataPoints);

        graph.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
        graph.getGridLabelRenderer().setVerticalAxisTitle(getResources().getString(R.string.graph_y));
        graph.getGridLabelRenderer().setHorizontalAxisTitle(getResources().getString(R.string.graph_x));

        series.setSpacing(0);
        series.setDataWidth(1);

        series.setDrawValuesOnTop(true);

        series.setValuesOnTopColor(Color.RED);

        lv = (ListView) findViewById(R.id.categoriesListView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listOfCategories );

        lv.setAdapter(arrayAdapter);

    }
}
