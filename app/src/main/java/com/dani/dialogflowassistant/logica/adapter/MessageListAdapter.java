package com.dani.dialogflowassistant.logica.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dani.dialogflowassistant.R;
import com.dani.dialogflowassistant.logica.async.AsyncImageLoad;
import com.dani.dialogflowassistant.logica.model.Message;
import com.dani.dialogflowassistant.logica.model.SendBird;
import com.dani.dialogflowassistant.logica.util.Utils;
import com.google.cloud.dialogflow.v2.Intent;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_CARD_MESSAGE = 2;
    private static final int VIEW_TYPE_TEXT_MESSAGE = 3;

    private List<Message> mMessageList;

    public MessageListAdapter(List<Message> messageList) {
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);

        if (message.getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            if (message.getType().equals(Intent.Message.MessageCase.CARD)) {
                return VIEW_TYPE_CARD_MESSAGE;
            } else {
                return VIEW_TYPE_TEXT_MESSAGE;
            }
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            if (viewType == VIEW_TYPE_CARD_MESSAGE) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received_card, parent, false);
                return new ReceivedCardMessageHolder(view);
            } else {
                if (mMessageList.size() == 0) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_message_received_text, parent, false);
                } else {
                    if (mMessageList.get(mMessageList.size() - 1).getSender().getNickname().equals(
                            "Asistente")) {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_message_received_text_without_user, parent, false);
                    } else {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_message_received_text, parent, false);
                    }
                }
                return new ReceivedTextMessageHolder(view);
            }

        }
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_CARD_MESSAGE:
                ((ReceivedCardMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_TEXT_MESSAGE:
                ((ReceivedTextMessageHolder) holder).bind(message);
                break;
            default:
                break;
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView messageText, timeText;

        private SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        private void bind(Message message) {
            messageText.setText(message.getMessage().getText().getText(0));

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        }
    }

    private class ReceivedTextMessageHolder extends RecyclerView.ViewHolder {
        private TextView messageText, timeText, nameText;

        private ReceivedTextMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
        }

        private void bind(Message message) {
            messageText.setText(message.getMessage().getText().getText(0));

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));

            nameText.setText(message.getSender().getNickname());
        }
    }

    private class ReceivedCardMessageHolder extends RecyclerView.ViewHolder {
        private TextView title, subtitle, timeText, nameText;
        private Button button;
        private ImageView image;

        private ReceivedCardMessageHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.card_title);
            subtitle = itemView.findViewById(R.id.card_subtitle);
            button = itemView.findViewById(R.id.card_button);
            timeText = itemView.findViewById(R.id.card_message_time);
            nameText = itemView.findViewById(R.id.card_message_name);
            image = itemView.findViewById(R.id.card_image);
        }

        private void bind(Message message) {
            title.setText(message.getMessage().getCard().getTitle());
            subtitle.setText(message.getMessage().getCard().getTitle());
            button.setText(message.getMessage().getCard().getButtons(0).getText());

            String[] extras = {message.getMessage().getCard().getButtons(0).getPostback()};
            button.setTag(extras);

            new AsyncImageLoad(image).execute(message.getMessage().getCard().getImageUri());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));

            nameText.setText(message.getSender().getNickname());
        }
    }
}
