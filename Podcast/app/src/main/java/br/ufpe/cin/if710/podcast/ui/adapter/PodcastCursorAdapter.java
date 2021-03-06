package br.ufpe.cin.if710.podcast.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

import br.ufpe.cin.if710.podcast.Extras.PodcastItemCurrentState;
import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.listeners.PodcastItemClickListener;

/**
 * Created by acpr on 07/10/17.
 */

public class PodcastCursorAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private PodcastItemClickListener itemClickListener;
    private String labelEscutar;
    private String labelBaixar;
    private String labelBaixando;
    private String labelEscutando;

    public PodcastCursorAdapter(PodcastItemClickListener listener, Context context, Cursor c, LayoutInflater mLayoutInflater) {
        super(context, c);
        this.mLayoutInflater = mLayoutInflater;
        itemClickListener = listener;
        labelEscutar = context.getString(R.string.action_listen);
        labelBaixar = context.getString(R.string.action_download);
        labelBaixando = context.getString(R.string.action_downloading);
        labelEscutando = context.getString(R.string.action_playing);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflater.from(context).inflate(R.layout.itemlista,null);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        TextView txtViewTitle = (TextView) view.findViewById(R.id.item_title);
        TextView txtViewDate = (TextView) view.findViewById(R.id.item_date);
        final Button action = (Button) view.findViewById(R.id.item_action);

        String title = cursor.getString(cursor.getColumnIndex(PodcastDBHelper.EPISODE_TITLE));
        String date = cursor.getString(cursor.getColumnIndex(PodcastDBHelper.EPISODE_DATE));

        //Ajeitar modelo para refletir todos os estados do podcast
        String episodeFileURI = cursor.getString(cursor.getColumnIndex(PodcastDBHelper.EPISODE_FILE_URI));

        txtViewTitle.setText(title);
        txtViewDate.setText(date);

        action.setText(episodeFileURI.isEmpty() ? labelBaixar : labelEscutar);
        final PodcastItemCurrentState state = episodeFileURI.isEmpty() ? PodcastItemCurrentState.INTHECLOUD : PodcastItemCurrentState.DOWNLOADED;

        txtViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.userRequestedEpisodeDetails(cursor.getPosition());
            }
        });
        txtViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.userRequestedEpisodeDetails(cursor.getPosition());
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.userRequestedPodcastItemAction(state,cursor.getPosition());
            }
        });
    }
}
