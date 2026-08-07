package app.mak.atmosense.core.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import app.mak.atmosense.core.data.UserSettingsProto
import com.google.protobuf.InvalidProtocolBufferException
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.io.InputStream
import java.io.OutputStream

@SingleIn(AppScope::class)
@Inject
class UserSettingsSerializer : Serializer<UserSettingsProto> {
  override val defaultValue: UserSettingsProto = UserSettingsProto.getDefaultInstance()

  override suspend fun readFrom(input: InputStream): UserSettingsProto {
    try {
      return UserSettingsProto.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
      throw CorruptionException("Cannot read proto.", exception)
    }
  }

  override suspend fun writeTo(t: UserSettingsProto, output: OutputStream) {
    t.writeTo(output)
  }
}
