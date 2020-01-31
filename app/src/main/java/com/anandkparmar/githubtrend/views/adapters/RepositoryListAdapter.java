package com.anandkparmar.githubtrend.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anandkparmar.githubtrend.R;
import com.anandkparmar.githubtrend.network.responses.TrendingRepositoriesResponse;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RepositoryListAdapter extends BaseExpandableListAdapter {
    private List<TrendingRepositoriesResponse> repositoryList;
    private Context context;
    private boolean sortedByStars = false;

    public RepositoryListAdapter(Context context, List<TrendingRepositoriesResponse> repositoryList) {
        this.context = context;
        this.repositoryList = repositoryList;
    }

    public void updateData(List<TrendingRepositoriesResponse> repositoryList) {
        this.repositoryList = repositoryList;
        this.sortData(sortedByStars);
    }

    @SuppressLint("NewApi")
    public void sortData(boolean sortedByStars) {
        this.sortedByStars = sortedByStars;
        if (sortedByStars) {
            repositoryList.sort((o1, o2) -> o1.getStars() >= o2.getStars() ? -1 : 1);
            this.notifyDataSetChanged();
        } else {
            repositoryList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getGroupCount() {
        return repositoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return repositoryList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return repositoryList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return repositoryList.get(groupPosition).getName().hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return repositoryList.get(groupPosition).getName().hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TrendingRepositoriesResponse repository = (TrendingRepositoriesResponse) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_view_repository, null);
        }

        CircleImageView repoImage = convertView.findViewById(R.id.repo_image);
        TextView repoAuthor = convertView.findViewById(R.id.repo_author);
        TextView repoName = convertView.findViewById(R.id.repo_name);

        repoAuthor.setText(repository.getAuthor());
        repoName.setText(repository.getName());
        Picasso.get().load(repository.getAvatar()).placeholder(R.drawable.shimmer_circle_bg).into(repoImage);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TrendingRepositoriesResponse repository = (TrendingRepositoriesResponse) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_view_repository_details, null);
        }

        TextView repoDescription = convertView.findViewById(R.id.repo_description);
        LinearLayout repoLangLayout = convertView.findViewById(R.id.repo_lang_layout);
        ImageView repoLangColor = convertView.findViewById(R.id.repo_lang_color);
        TextView repoLang = convertView.findViewById(R.id.repo_lang);
        TextView repoStars = convertView.findViewById(R.id.repo_stars);
        TextView repoForks = convertView.findViewById(R.id.repo_forks);


        if (repository.getDescription() != null && !repository.getDescription().isEmpty()) {
            repoDescription.setText(repository.getDescription());
            repoDescription.setVisibility(View.VISIBLE);
        } else {
            repoDescription.setVisibility(View.GONE);
        }

        if (repository.getLanguage() != null && !repository.getLanguage().isEmpty()) {
            repoLang.setText(repository.getLanguage());
            if (repository.getLanguageColor() != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                repoLangColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(repository.getLanguageColor())));
            }
            repoLangLayout.setVisibility(View.VISIBLE);
        } else {
            repoLangLayout.setVisibility(View.GONE);
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        repoStars.setText(formatter.format(repository.getStars()));
        repoForks.setText(formatter.format(repository.getForks()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
