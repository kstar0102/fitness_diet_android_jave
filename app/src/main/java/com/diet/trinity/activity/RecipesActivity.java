package com.diet.trinity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.diet.trinity.Adapter.RecyclerViewAdapter;
import com.diet.trinity.R;
import com.diet.trinity.model.Recipe;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        addEventListener();
        initData();
    }

    private void addEventListener(){
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void initData(){
        ArrayList<Recipe> recipeList1 = new ArrayList<>();
        recipeList1.add(new Recipe(1, "Φρουτοσαλάτα με επιλεγμένα..", R.drawable.back_recipe1, 2));
        recipeList1.add(new Recipe(1, "Κοτόπουλο με μανιτάρια", R.drawable.back_recipe2, 4));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recCategory1);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, recipeList1);
        recyclerView.setAdapter(adapter);

        ArrayList<Recipe> recipeList2 = new ArrayList<>();
        recipeList2.add(new Recipe(2, "Ζελέ με φράουλες και ροδάκινο..", R.drawable.back_recipe3, 1));
        recipeList2.add(new Recipe(2, "Τυρί κρέμα με bagel", R.drawable.back_recipe4, 2));

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView2 = findViewById(R.id.recCategory2);
        recyclerView2.setLayoutManager(layoutManager2);
        RecyclerViewAdapter adapter2 = new RecyclerViewAdapter(this, recipeList2);
        recyclerView2.setAdapter(adapter2);

        ArrayList<Recipe> recipeList3 = new ArrayList<>();
        recipeList3.add(new Recipe(3, "Χταποδάκι με ρύζι και..", R.drawable.back_recipe5, 2));
        recipeList3.add(new Recipe(4, "Κοτόπουλο με μανιτάρια", R.drawable.back_recipe2, 4));

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView3 = findViewById(R.id.recCategory3);
        recyclerView3.setLayoutManager(layoutManager3);
        RecyclerViewAdapter adapter3 = new RecyclerViewAdapter(this, recipeList3);
        recyclerView3.setAdapter(adapter3);
    }
}