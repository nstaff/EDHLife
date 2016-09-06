package com.staffend.nicholas.lifecounter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.staffend.nicholas.lifecounter.R;

/**
 * Layout class for player views
 * Created by Nicholas on 12/17/2015.
 */
public class EDHPlayerViewGroup extends LinearLayout {
    public final int playerContainerId;
    public final int commanderContainerId;


    public EDHPlayerViewGroup(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.edh_player_viewgroup_layout, this, true);

        //This happens because many of these are created at runtime with the same resource
        // designator. This will prevent conflicts
        LinearLayout playerContainer = (LinearLayout) findViewById(R.id.player_life_holder);
        playerContainer.setId(View.generateViewId());
        this.playerContainerId = playerContainer.getId();
        LinearLayout commanderContainer = (LinearLayout) findViewById(R.id.commanderDamageContainer);
        commanderContainer.setId(View.generateViewId());
        this.commanderContainerId = commanderContainer.getId();

    }
}
