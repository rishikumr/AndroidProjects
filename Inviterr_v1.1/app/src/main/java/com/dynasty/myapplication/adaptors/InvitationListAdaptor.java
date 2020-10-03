package com.dynasty.myapplication.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dynasty.myapplication.R;
import com.dynasty.myapplication.entity.People;
import java.util.ArrayList;

public class InvitationListAdaptor extends RecyclerView.Adapter<InvitationListAdaptor.InvitationListHolder> {


    private ArrayList<People> guest_invitation_list;

    public InvitationListAdaptor(ArrayList<People> guest_invitation_list) {
        super();
        this.guest_invitation_list = guest_invitation_list;
    }



    @NonNull
    @Override
    public InvitationListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate( R.layout.person_invitation_details ,parent , false);
        InvitationListHolder rcv = new InvitationListHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationListHolder holder, int position) {
        holder.guest_name.setText(guest_invitation_list.get(position).getguest_name());
        holder.guest_contact_info.setText(guest_invitation_list.get(position).getguest_number());
        holder.guest_invitation_status.setText(guest_invitation_list.get(position).getGuest_acceptance_String());

    }

    @Override
    public int getItemCount() {
        return guest_invitation_list.size();
    }

    public class InvitationListHolder extends RecyclerView.ViewHolder {
        TextView guest_name , guest_contact_info , guest_invitation_status;
        public InvitationListHolder(@NonNull View itemView) {
            super(itemView);
            guest_name = itemView.findViewById(R.id.cv_person_name);
            guest_contact_info = itemView.findViewById(R.id.cv_person_contact_info);
           guest_invitation_status = itemView.findViewById(R.id.cv_person_status);

            // itemView.setonClickListener()  for click action
        }
    }
}
