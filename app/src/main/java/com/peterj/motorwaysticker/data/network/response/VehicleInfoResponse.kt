import com.peterj.motorwaysticker.domain.model.Country
import com.peterj.motorwaysticker.domain.model.VehicleInfo
import kotlinx.serialization.Serializable

@Serializable
data class VehicleInfoResponse(
    val requestId: Long,
    val statusCode: String,
    val internationalRegistrationCode: String,
    val type: String,
    val name: String,
    val plate: String,
    val country: CountryResponse,
    val vignetteType: String
)

@Serializable
data class CountryResponse(
    val hu: String,
    val en: String
)

fun VehicleInfoResponse.toDomain(): VehicleInfo {
    return VehicleInfo(
        registrationCode = internationalRegistrationCode,
        type = type,
        ownerName = name,
        plate = plate,
        country = Country(
            hu = country.hu,
            en = country.en,
        ),
        vignetteType = vignetteType
    )
}
