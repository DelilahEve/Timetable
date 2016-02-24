package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.data.Color;

public class ColorPickerDialog implements SeekBar.OnSeekBarChangeListener {

    private ColorPick parent;

    private ArrayList<Color> favs;

    private AlertDialog.Builder builder;

    private LayoutInflater inflater;

    private View view;

    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;

    private View colorPreview;

    private GridView favourites;

    private TextView addFavButton;

    public ColorPickerDialog(Context context, ColorPick parent) {
        this.parent = parent;
        favs = new ArrayList<>();
        builder = new AlertDialog.Builder(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        builder.setTitle("Pick Color");

        View view = inflater.inflate(R.layout.dialog_pick_color, null, false);

        red = (SeekBar) view.findViewById(R.id.redBar);
        green = (SeekBar) view.findViewById(R.id.greenBar);
        blue = (SeekBar) view.findViewById(R.id.blueBar);

        red.setOnSeekBarChangeListener(this);

        colorPreview = view.findViewById(R.id.colorPreview);

        favourites = (GridView) view.findViewById(R.id.favouriteColors);

        addFavButton = (TextView) view.findViewById(R.id.addFavourite);

        addFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavourite();
            }
        });

        builder.setView(view);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });

        builder.setNegativeButton("Cancel", null);
    }

    public void show() {
        builder.create().show();
    }

    private void save() {
        parent.setColor(new Color(red.getProgress(), green.getProgress(), blue.getProgress()));
    }

    private void addFavourite() {
        int r, g, b;

        // Get color here
        r = red.getProgress();
        g = green.getProgress();
        b = blue.getProgress();

        favs.add(new Color(r, g, b));
    }

    private void setColor(Color c) {
        red.setProgress(c.getRed());
        green.setProgress(c.getGreen());
        blue.setProgress(c.getBlue());

        colorPreview.setBackgroundColor(c.makeColor());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // update color
        setColor(new Color(red.getProgress(), green.getProgress(), blue.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
