package com.dani.dialogflowassistant.logica.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dani.dialogflowassistant.R;
import com.dani.dialogflowassistant.logica.async.AsyncImageLoad;
import com.dani.dialogflowassistant.logica.model.Message;
import com.dani.dialogflowassistant.logica.model.SendBird;
import com.dani.dialogflowassistant.logica.util.Utils;
import com.dani.dialogflowassistant.vista.MainActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.cloud.dialogflow.v2.Intent;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_CARD_MESSAGE = 2;
    private static final int VIEW_TYPE_TEXT_MESSAGE = 3;
    private static final int VIEW_TYPE_TEXT_MESSAGE_WITHOUT_USER = 4;
    private static final int VIEW_TYPE_SUGGESTION_MESSAGE = 5;

    private List<Message> mMessageList;
    private MainActivity activity;

    public MessageListAdapter(List<Message> messageList, MainActivity activity) {
        mMessageList = messageList;
        this.activity = activity;
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
            } else if (message.getType().equals(Intent.Message.MessageCase.SUGGESTIONS)) {
                return VIEW_TYPE_SUGGESTION_MESSAGE;
            } else {
                if (position > 0 && mMessageList.get(position - 1).getSender().getNickname().equals(
                        "Asistente")) {
                    return VIEW_TYPE_TEXT_MESSAGE_WITHOUT_USER;
                } else {
                    return VIEW_TYPE_TEXT_MESSAGE;
                }
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
            } else if (viewType == VIEW_TYPE_SUGGESTION_MESSAGE) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received_chip, parent, false);
                return new ReceivedSuggestionMessageWithoutUserHolder(view);
            } else if (viewType == VIEW_TYPE_TEXT_MESSAGE) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received_text, parent, false);
                return new ReceivedTextMessageHolder(view);
            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received_text_without_user, parent, false);
                return new ReceivedTextMessageWithoutUserHolder(view);
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
            case VIEW_TYPE_TEXT_MESSAGE_WITHOUT_USER:
                ((ReceivedTextMessageWithoutUserHolder) holder).bind(message);
                break;
            case VIEW_TYPE_SUGGESTION_MESSAGE:
                ((ReceivedSuggestionMessageWithoutUserHolder) holder).bind(message);
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
            String formatedText =
                    message.getMessage().getText().getText(0).substring(0, 1).toUpperCase() +
                            message.getMessage().getText().getText(0).substring(1);
            messageText.setText(formatedText);

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));

            nameText.setText(message.getSender().getNickname());
        }
    }

    private class ReceivedTextMessageWithoutUserHolder extends RecyclerView.ViewHolder {
        private TextView messageText, timeText;

        private ReceivedTextMessageWithoutUserHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body_without_user);
            timeText = itemView.findViewById(R.id.text_message_time_without_user);
        }

        private void bind(Message message) {
            String formatedText =
                    message.getMessage().getText().getText(0).substring(0, 1).toUpperCase() +
                            message.getMessage().getText().getText(0).substring(1);
            messageText.setText(formatedText);

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        }
    }

    private class ReceivedSuggestionMessageWithoutUserHolder extends RecyclerView.ViewHolder {
        private ChipGroup chipGroup;

        private ReceivedSuggestionMessageWithoutUserHolder(View itemView) {
            super(itemView);

            chipGroup = itemView.findViewById(R.id.chipGroup);
        }

        private void bind(Message message) {
            for (int i = 0; i < message.getMessage().getSuggestions().getSuggestionsCount(); i++) {
                Chip chip = new Chip(itemView.getContext());
                chip.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                chip.setText(message.getMessage().getSuggestions().getSuggestions(i).getTitle());
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.colorMessageRecieved)));
                chip.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.categorySelect(v);
                    }
                });
                chipGroup.addView(chip);
            }
        }
    }

    private class ReceivedCardMessageHolder extends RecyclerView.ViewHolder {
        private TextView title, subtitle, timeText, nameText;
        private ImageView image;

        private ReceivedCardMessageHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.card_title);
            subtitle = itemView.findViewById(R.id.card_subtitle);
            timeText = itemView.findViewById(R.id.card_message_time);
            nameText = itemView.findViewById(R.id.card_message_name);
            image = itemView.findViewById(R.id.card_image);
        }

        private void bind(Message message) {
            title.setText(message.getMessage().getCard().getTitle());
            subtitle.setText(message.getMessage().getCard().getTitle());

            new AsyncImageLoad(image).execute(message.getMessage().getCard().getImageUri());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));

            nameText.setText(message.getSender().getNickname());
        }
    }
}
