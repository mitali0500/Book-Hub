package fragment

import adapter.DashboardRecyclerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mitali.bookhub.R
import model.Book
import org.json.JSONException
import util.ConnectionManager
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap



class DashboardFragment : Fragment() {
    lateinit var recycleDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var  progressBar: ProgressBar


    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1, book2 ->

        if (book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            // sort according to name if rating is same
            book1.bookName.compareTo(book2.bookName, true)
        } else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }
    // how the comparator class works?
    //stringone.compareTo(stringtwo,true)
    //-> if the string are identical, the result will be zero
    //-> if the rating of book1 is greater than book2 the result will be +ve.
    //-> if the rating of book1 is lesser than book2 the result will be -ve.



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)

        recycleDashboard = view!!.findViewById(R.id.recycleDashboard)

        layoutManager = LinearLayoutManager(activity)


        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE // visibility method and set the view to visible




        val queue = Volley.newRequestQueue(activity as Context)
        // a simple variable queue for a request queue

        val url = "http://13.235.250.119/v1/book/fetch_books/"//correct checked
        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET,url,null, Response.Listener {
//Here we will handle the response
                try {
                    progressLayout.visibility = View.GONE
                    val success = it.getBoolean("success")

                    if(success)
                    {
                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)

                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context,bookInfoList)

                            recycleDashboard.adapter = recyclerAdapter

                            recycleDashboard.layoutManager = layoutManager


                        }

                    }
                    else{
                        Toast.makeText(activity as Context,"Some error occurred!!",Toast.LENGTH_SHORT).show()
                    }

                } catch (e:JSONException){
                    Toast.makeText(activity as Context,"Some error is occurred!!",Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener {
// Here we will handle the errors Volley errors are handled here
                if(activity != null){
                    Toast.makeText(activity as Context,"Volley Error Occurred!!", Toast.LENGTH_SHORT).show()
                }


            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String ,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "1989c707469c47"
                    return headers
                }// header syntax is correct

            }
            queue.add(jsonObjectRequest)
        }else{
            val dialog =AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
// Implicit intent -> is used to open things that are present on the phone, but are outside of the app.
                //earlier we use an intent to open a new activity/view that was Explicit intent.
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val  id = item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }


}
// val jsonObjectRequest = object: JsonObjectRequest(){}
//it is the syntax of declaring a json object
// val variable_name = object:JsonObjectRequest(){}
//objects creating this in syntax are known anonymous objects. when we want to override a method of a class
//with very few modifications
// type of requests in volley library
// json request - they are used when want to fetch json responses from the server
// two types of it -> JsonObject request & JsonArray request
// string request
// val stringRequest =StringRequest(RequestMethod,url,responseListener{},ErrorListener{}


