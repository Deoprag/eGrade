package com.deopraglabs.egrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.util.EGradeUtil;

import java.util.List;

public class CoordinatorAdapter extends RecyclerView.Adapter<CoordinatorAdapter.CoordinatorViewHolder> {

    private Context context;
    private List<Coordinator> coordinatorList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Coordinator coordinator);
    }

    public CoordinatorAdapter(Context context, List<Coordinator> coordinatorList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.coordinatorList = coordinatorList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CoordinatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coordinator, parent, false);
        return new CoordinatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoordinatorViewHolder holder, int position) {
        Coordinator coordinator = coordinatorList.get(position);

        holder.nameTextView.setText(coordinator.getName());
        holder.cpfTextView.setText(EGradeUtil.formatCpf(coordinator.getCpf()));
        holder.emailTextView.setText(coordinator.getEmail());
        holder.phoneTextView.setText(EGradeUtil.formatNumber(coordinator.getPhoneNumber()));
        holder.birthDateTextView.setText(EGradeUtil.dateToString(coordinator.getBirthDate()));
        holder.activeTextView.setText(coordinator.isActive() ? "Ativo" : "Inativo");
        if (coordinator.getProfilePicture() != null) {
            holder.profileImageView.setImageBitmap(EGradeUtil.base64ToBitmap(coordinator.getProfilePicture()));
        } else {
            holder.profileImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile_placeholder));
        }
        holder.editButton.setOnClickListener(v -> onItemClickListener.onItemClick(coordinator));
    }

    @Override
    public int getItemCount() {
        return coordinatorList.size();
    }

    public static class CoordinatorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, cpfTextView, emailTextView, phoneTextView, birthDateTextView, activeTextView;
        ImageView profileImageView;
        ImageButton editButton;

        public CoordinatorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            cpfTextView = itemView.findViewById(R.id.cpfTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            birthDateTextView = itemView.findViewById(R.id.birthDateTextView);
            activeTextView = itemView.findViewById(R.id.activeTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
