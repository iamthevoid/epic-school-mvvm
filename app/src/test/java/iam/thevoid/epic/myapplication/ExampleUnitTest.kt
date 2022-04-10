package iam.thevoid.epic.myapplication

import com.google.gson.Gson
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.data.model.Country
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val gson = Gson()
        val country: Country = gson.fromJson(json, Country::class.java)
        println(gson.toJson(country))
        Observable.just("fr", "us", "ru")
            .flatMapSingle(CountriesClient.api::byCode)
            .toList()
            .blockingSubscribe({ countries ->
                println(countries)
            }, Throwable::printStackTrace)
    }
}

val json = "{\n" +
        "\t\"name\": \"France\",\n" +
        "\t\"topLevelDomain\": [\n" +
        "\t\t\".fr\"\n" +
        "\t],\n" +
        "\t\"alpha2Code\": \"FR\",\n" +
        "\t\"alpha3Code\": \"FRA\",\n" +
        "\t\"callingCodes\": [\n" +
        "\t\t\"33\"\n" +
        "\t],\n" +
        "\t\"capital\": \"Paris\",\n" +
        "\t\"altSpellings\": [\n" +
        "\t\t\"FR\",\n" +
        "\t\t\"French Republic\",\n" +
        "\t\t\"République française\"\n" +
        "\t],\n" +
        "\t\"subregion\": \"Western Europe\",\n" +
        "\t\"region\": \"Europe\",\n" +
        "\t\"population\": 67391582,\n" +
        "\t\"latlng\": [\n" +
        "\t\t46.0,\n" +
        "\t\t2.0\n" +
        "\t],\n" +
        "\t\"demonym\": \"French\",\n" +
        "\t\"area\": 640679.0,\n" +
        "\t\"gini\": 32.4,\n" +
        "\t\"timezones\": [\n" +
        "\t\t\"UTC-10:00\",\n" +
        "\t\t\"UTC-09:30\",\n" +
        "\t\t\"UTC-09:00\",\n" +
        "\t\t\"UTC-08:00\",\n" +
        "\t\t\"UTC-04:00\",\n" +
        "\t\t\"UTC-03:00\",\n" +
        "\t\t\"UTC+01:00\",\n" +
        "\t\t\"UTC+02:00\",\n" +
        "\t\t\"UTC+03:00\",\n" +
        "\t\t\"UTC+04:00\",\n" +
        "\t\t\"UTC+05:00\",\n" +
        "\t\t\"UTC+10:00\",\n" +
        "\t\t\"UTC+11:00\",\n" +
        "\t\t\"UTC+12:00\"\n" +
        "\t],\n" +
        "\t\"borders\": [\n" +
        "\t\t\"AND\",\n" +
        "\t\t\"BEL\",\n" +
        "\t\t\"DEU\",\n" +
        "\t\t\"ITA\",\n" +
        "\t\t\"LUX\",\n" +
        "\t\t\"MCO\",\n" +
        "\t\t\"ESP\",\n" +
        "\t\t\"CHE\"\n" +
        "\t],\n" +
        "\t\"nativeName\": \"France\",\n" +
        "\t\"numericCode\": \"250\",\n" +
        "\t\"flags\": {\n" +
        "\t\t\"svg\": \"https://flagcdn.com/fr.svg\",\n" +
        "\t\t\"png\": \"https://flagcdn.com/w320/fr.png\"\n" +
        "\t},\n" +
        "\t\"currencies\": [\n" +
        "\t\t{\n" +
        "\t\t\t\"code\": \"EUR\",\n" +
        "\t\t\t\"name\": \"Euro\",\n" +
        "\t\t\t\"symbol\": \"€\"\n" +
        "\t\t}\n" +
        "\t],\n" +
        "\t\"languages\": [\n" +
        "\t\t{\n" +
        "\t\t\t\"iso639_1\": \"fr\",\n" +
        "\t\t\t\"iso639_2\": \"fra\",\n" +
        "\t\t\t\"name\": \"French\",\n" +
        "\t\t\t\"nativeName\": \"français\"\n" +
        "\t\t}\n" +
        "\t],\n" +
        "\t\"translations\": {\n" +
        "\t\t\"br\": \"França\",\n" +
        "\t\t\"pt\": \"França\",\n" +
        "\t\t\"nl\": \"Frankrijk\",\n" +
        "\t\t\"hr\": \"Francuska\",\n" +
        "\t\t\"fa\": \"فرانسه\",\n" +
        "\t\t\"de\": \"Frankreich\",\n" +
        "\t\t\"es\": \"Francia\",\n" +
        "\t\t\"fr\": \"France\",\n" +
        "\t\t\"ja\": \"フランス\",\n" +
        "\t\t\"it\": \"Francia\",\n" +
        "\t\t\"hu\": \"Franciaország\"\n" +
        "\t},\n" +
        "\t\"flag\": \"https://flagcdn.com/fr.svg\",\n" +
        "\t\"regionalBlocs\": [\n" +
        "\t\t{\n" +
        "\t\t\t\"acronym\": \"EU\",\n" +
        "\t\t\t\"name\": \"European Union\"\n" +
        "\t\t}\n" +
        "\t],\n" +
        "\t\"cioc\": \"FRA\",\n" +
        "\t\"independent\": true\n" +
        "}"