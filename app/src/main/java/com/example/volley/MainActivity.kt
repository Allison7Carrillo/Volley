package com.example.volley

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    var miRecyclerView:RecyclerView?=null
    var adaptadorpaises:PaisesAdapter?=null
    var listaPaises:ArrayList<Pais>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listaPaises= ArrayList<Pais>()
        adaptadorpaises= PaisesAdapter(listaPaises!!,this)
        miRecyclerView=findViewById(R.id.miRecyclerCovid)
        miRecyclerView!!.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        miRecyclerView!!.adapter= adaptadorpaises

        val queue = Volley.newRequestQueue(this)
        val url = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/latest"

       val peticionDatosCovid= JsonArrayRequest(Request.Method.GET,url,null,Response.Listener { response ->
          for(index in 0..response.length()-1){

              try {
                  val paisJson= response.getJSONObject(index)
                  val nombrePais= paisJson.getString("countryregion")
                  val numeroConfirmados=paisJson.getInt("confirmed")
                  val numeroMuertos= paisJson.getInt("deaths")
                  val numeroRecuperados=paisJson.getInt("recovered")
                  val countrycodeJson=paisJson.getJSONObject("countrycode")
                  val codigoPais= countrycodeJson.getString("iso2")
                  //objeto de kotlin
                  val paisIndividual=Pais(nombrePais,numeroConfirmados,numeroMuertos,numeroRecuperados,codigoPais)
                  listaPaises!!.add(paisIndividual)
              }catch (e:JSONException){
                    Log.wtf("Json error", e.localizedMessage)
              }

       }
           listaPaises!!.sortByDescending { it.confirmados }
           adaptadorpaises!!.notifyDataSetChanged()
       },

           Response.ErrorListener { error ->
               Log.e("error_volley",error.localizedMessage)

           })

          queue.add(peticionDatosCovid)


    }
}