/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.charting.engine.Series");
dojo.require("dojo.lang.common");
dojo.require("dojo.widget.charting.engine.Plotters");

dojo.widget.charting.engine.Series = function(/* object? */kwArgs){
	var args = kwArgs || { length:1 };
	this.dataSource = args.dataSource || null;
	this.bindings = { };
	this.color = args.color;
	this.label = args.label;

	if(args.bindings){
		for(var p in args.bindings){
			this.addBinding(p, args.bindings[p]);
		}
	}
};

dojo.extend(dojo.widget.charting.engine.Series, {
	bind:function(src, bindings){
		this.dataSource = src;
		this.bindings = bindings;
		this.onBind();
	},
	addBinding:function(name, binding){
		this.bindings[name] = binding;
	},
	evaluate:function(kwArgs){
		var ret = [];
		var a = this.dataSource.getData();
		var l = a.length;
		var start = 0;
		var end = l;
		
		/*	Allow for ranges.  Can be done in one of two ways:
		 *	1. { from, to } as 0-based indices
		 *	2. { length } as num of data points to get; a negative
		 *		value will start from the end of the data set.
		 *	No kwArg object means the full data set will be evaluated
		 *		and returned.
		 */
		if(kwArgs){
			if(kwArgs.from){ 
				start = Math.max(kwArgs.from,0);
				if(kwArgs.to){ 
					end = Math.min(kwArgs.to, end);
				}
			}
			else if(kwArgs.length){
				if(kwArgs.length < 0){
					//	length points from end
					start = Math.max((end + length),0);
				} else {
					end = Math.min((start + length), end);
				}
			}
		}

		for(var i=start; i<end; i++){
			var o = { src: a[i], series: this };
			for(var p in this.bindings){
				o[p] = this.dataSource.getField(a[i], this.bindings[p]);
			}
			ret.push(o);
		}

		//	sort by the x axis, if available.
		if(typeof(ret[0].x) != "undefined"){
			ret.sort(function(a,b){
				if(a.x > b.x) return 1;
				if(a.x < b.x) return -1;
				return 0;
			});
		}
		return ret;	//	array
	},

	//	trends
	trends:{
		createRange: function(values, len){
			var idx = values.length-1;
			var length = (len||values.length);
			return { "index": idx, "length": length, "start":Math.max(idx-length,0) };
		},

		mean: function(values, len){
			var range = this.createRange(values, len);
			if(range.index<0){ return 0; }
			var total = 0;
			var count = 0;
			for(var i=range.index; i>=range.start; i--){
				total += values[i].y; 
				count++;
			}
			total /= Math.max(count,1);
			return total;
		},

		variance: function(values,len){
			var range = this.createRange(values,len);
			if(range.index < 0){ return 0; }
			var total = 0;
			var square = 0;
			var count = 0;
			for(var i=range.index; i>=range.start; i--){
				total += values[i].y;
				square += Math.pow(values[i].y, 2);
				count++;
			}
			return (square/count)-Math.pow(total/count,2);
		},

		standardDeviation: function(values, len){
			return Math.sqrt(this.getVariance(values, len));
		},

		max: function(values, len){
			var range = this.createRange(values, len);
			if(range.index < 0){ return 0; }
			var max = Number.MIN_VALUE;
			for (var i=range.index; i>=range.start; i--){
				max = Math.max(values[i].y,max);
			}
			return max;
		},

		min: function(values, len){
			var range=this.createRange(values, len);
			if(range.index < 0){ return 0; }
			var min = Number.MAX_VALUE;
			for(var i=range.index; i>=range.start; i--){
				min = Math.min(values[i].y, min);
			}
			return min;
		},

		median: function(values, len){
			var range = this.createRange(values, len);
			if(range.index<0){ return 0; }
			var a = [];
			for (var i=range.index; i>=range.start; i--){
				var b=false;
				for(var j=0; j<a.length; j++){
					if(values[i].y == a[j]){
						b = true;
						break;
					}
				}
				if(!b){ 
					a.push(values[i].y); 
				}
			}
			a.sort();
			if(a.length > 0){ 
				return a[Math.ceil(a.length / 2)]; 
			}
			return 0;
		},

		mode: function(values, len){
			var range=this.createRange(values, len);
			if(range.index<0){ return 0; }
			var o = {};
			var ret = 0
			var median = Number.MIN_VALUE;
			for(var i=range.index; i>=range.start; i--){
				if (!o[values[i].y]){
					o[values[i].y] = 1;
				} else { 
					o[values[i].y]++;
				}
			}
			for(var p in o){
				if(median < o[p]){ 
					median = o[p]; 
					ret=p; 
				}
			}
			return ret;
		}
	},

	//	events
	onBind:function(){ }
});
