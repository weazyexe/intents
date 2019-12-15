package exe.weazy.intents.recycler.diffutil

import androidx.recyclerview.widget.DiffUtil
import exe.weazy.intents.entity.IntentEntity

class IntentDiffUtilItemCallback : DiffUtil.ItemCallback<IntentEntity>() {

    override fun areItemsTheSame(oldItem: IntentEntity, newItem: IntentEntity) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: IntentEntity, newItem: IntentEntity) = oldItem == newItem
}