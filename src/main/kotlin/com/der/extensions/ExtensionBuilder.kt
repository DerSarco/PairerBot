
import com.der.extensions.pairing.MatchExtension
import com.der.extensions.pairing.PairExtension
import dev.kord.core.entity.ReactionEmoji

class BotExtensionBuilder {

    object EMOJI {
        val emoji = ReactionEmoji.Unicode("✅")
    }

    object AvailableRoles {
        val ENABLED_ROLES = mutableListOf("ADMIN", "administrador", "admin", "#GabiMoreno")
    }


    val matchExtension = MatchExtension()
    val pairExtension = PairExtension()

}