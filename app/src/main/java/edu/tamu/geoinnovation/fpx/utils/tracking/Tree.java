package edu.tamu.geoinnovation.fpx.utils.tracking;
/*
 Author: Jose Ramos

 */

public class Tree{

	Double timeRange;
    Double meanX; 
    Double stdDevX; 
    Double maxX; 
    Double minX; 
    Double meanY;
    Double stdDevY; 
    Double maxY; 
    Double minY; 
    Double meanZ; 
    Double stdDevZ; 
    Double maxZ; 
    Double minZ;
    Double meanX2; 
    Double stdDevX2;
    Double maxX2 ;
    Double minX2; 
    Double meanY2; 
    Double stdDevY2; 
    Double maxY2; 
    Double minY2; 
    Double meanZ2; 
    Double stdDevZ2; 
    Double maxZ2;
    Double minZ2; 
    Double meanEuc; 
    Double stdDevEuc;
    Double maxEuc; 
    Double minEuc; 
    Double meanEuc2; 
    Double stdDevEuc2; 
    Double maxEuc2; 
    Double minEuc2; 
    Double meanJerk; 
    Double stdDevJerk; 
    Double maxJerk; 
    Double MinJerk;  

    String pred_activity = "";

    public Tree(Double timeRange,Double meanX, Double stdDevX, Double maxX, Double minX, Double meanY,
    			Double stdDevY, Double maxY, Double minY, Double meanZ, Double stdDevZ, Double maxZ, Double minZ, Double meanX2,
   				Double stdDevX2, Double maxX2, Double minX2, Double meanY2, Double stdDevY2, Double maxY2, Double minY2, 
				Double meanZ2, Double stdDevZ2, Double maxZ2, Double minZ2, Double meanEuc, Double stdDevEuc, Double maxEuc, 
    			Double minEuc, Double meanEuc2, Double stdDevEuc2, Double maxEuc2, Double minEuc2, Double meanJerk, Double stdDevJerk,
    			Double maxJerk, Double MinJerk ){

    	this.timeRange = timeRange;
    	this.meanX     = meanX;  
    	this.stdDevX   = stdDevX; 
    	this.maxX      = maxX;  
    	this.minX      = minX; 
    	this.meanY     = meanY;
    	this.stdDevY   = stdDevY;  
    	this.maxY      = maxY;  
    	this.minY      = minY;  
    	this.meanZ     = meanZ;  
    	this.stdDevZ   = stdDevZ;  
    	this.maxZ      = maxZ;  
    	this.minZ      = minZ; 
    	this.meanX2    = meanX2;  
    	this.stdDevX2  = stdDevX2;  
    	this.maxX2     = maxX2; 
    	this.minX2     = minX2;  
    	this.meanY2    = meanY2;  
    	this.stdDevY2  = stdDevY2;  
    	this.maxY2     = maxY2;  
    	this.minY2     = minY2;  
    	this.meanZ2    = meanZ2;  
    	this.stdDevZ2  = stdDevZ2;  
    	this.maxZ2     = maxZ2; 
    	this.minZ2     = minZ2;  
    	this.meanEuc   = meanEuc;  
    	this.stdDevEuc = stdDevEuc;  
    	this.maxEuc    = maxEuc;  
    	this.minEuc    = minEuc;  
    	this.meanEuc2  = meanEuc2;  
    	this.stdDevEuc2 = stdDevEuc2;  
    	this.maxEuc2   = maxEuc2;  
    	this.minEuc2   = minEuc2;  
    	this.meanJerk  = meanJerk;  
    	this.stdDevJerk = stdDevJerk;  
    	this.maxJerk   = maxJerk;  
    	this.MinJerk   = MinJerk;   
    }

    public String getActivity(){

	if(stdDevY2 <= 407769.873427){
		if(meanY <= 163.335022){
			if(meanX2 <= 446342.185286){
				if(maxZ <= 872.293382){
					if(stdDevY2 <= 77075.557964){
						if(stdDevJerk <= 773.06651){
							pred_activity = "Curls" ;
						}else{
							if(meanZ <= -956.363874){
								if(timeRange <= 1.987){
									pred_activity = "No Workout" ;
								}else{
									pred_activity = "Bench Press" ;}
							}else{
								pred_activity = "No Workout" ;}
						}
					}else{
						if(minX2 <= 2148.814031){
							if(maxY <= 605.097434){
								if(stdDevY2 <= 257456.792721){
									if(stdDevJerk <= 1072.15902){
										pred_activity = "Situps" ;
									}else{
										pred_activity = "No Workout" ;}
								}else{
									pred_activity = "No Workout" ;}
							}else{
								pred_activity = "No Workout" ;}
						}else{
							pred_activity = "No Workout" ;}
					}
				}else{
					pred_activity = "Flys" ;}
			}else{
				if(maxZ <= 19.633626){
					if(maxX2 <= 1353882.641752){
						if(maxY2 <= 52691.036437){
							if(meanX <= 845.561467){
								if(stdDevZ2 <= 39407.1359){
									if(stdDevY <= 34.746408){
										if(meanZ <= -575.43859){
											pred_activity = "Bench Press" ;
										}else{
											if(meanEuc <= 971.513507){
												pred_activity = "Bench Press" ;
											}else{
												pred_activity = "Deadlifts" ;}
										}
									}else{
										pred_activity = "Pushups" ;}
								}else{
									if(minX2 <= 366547.803957){
										pred_activity = "No Workout" ;
									}else{
										if(maxX <= 892.177331){
											pred_activity = "Deadlifts" ;
										}else{
											if(stdDevEuc <= 63.52038){
												pred_activity = "Bench Press" ;
											}else{
												pred_activity = "Deadlifts" ;}
										}
									}
								}
							}else{
								if(maxY2 <= 23930.25982){
									if(minX <= 945.352749){
										if(stdDevZ <= 66.074262){
											pred_activity = "Bench Press" ;
										}else{
											pred_activity = "Deadlifts" ;}
									}else{
										pred_activity = "No Workout" ;}
								}else{
									pred_activity = "No Workout" ;}
							}
						}else{
							if(meanY2 <= 19766.580729){
								pred_activity = "Pulldowns" ;
							}else{
								if(maxY2 <= 239116.628254){
									pred_activity = "No Workout" ;
								}else{
									if(minY2 <= 256.414995){
										pred_activity = "No Workout" ;
									}else{
										pred_activity = "Flys" ;}
								}
							}
						}
					}else{
						if(meanZ <= -171.818149){
							if(maxY <= 267.802182){
								pred_activity = "Pulldowns" ;
							}else{
								pred_activity = "No Workout" ;}
						}else{
							pred_activity = "Step ups" ;}
					}
				}else{
					if(maxX2 <= 898762.013642){
						if(minEuc <= 908.866358){
							if(minZ <= -49.036796){
								if(minZ <= -364.923356){
									if(meanX <= 220.422512){
										pred_activity = "No Workout" ;
									}else{
										if(timeRange <= 1.979){
											pred_activity = "Flys" ;
										}else{
											pred_activity = "Bench Press" ;}
									}
								}else{
									if(minY <= -417.07717){
										if(maxX <= 457.110514){
											pred_activity = "Flys" ;
										}else{
											pred_activity = "Bench Press" ;}
									}else{
										pred_activity = "Flys" ;}
								}
							}else{
								if(meanY <= 68.688386){
									if(maxEuc <= 991.855482){
										pred_activity = "Flys" ;
									}else{
										if(stdDevX <= 108.036624){
											pred_activity = "Bench Press" ;
										}else{
											pred_activity = "Flys" ;}
									}
								}else{
									pred_activity = "Flys" ;}
							}
						}else{
							if(meanEuc <= 980.902088){
								pred_activity = "No Workout" ;
							}else{
								pred_activity = "Bench Press" ;}
						}
					}else{
						if(meanY2 <= 38175.544173){
							if(maxX <= 1323.097277){
								if(maxZ <= 272.285512){
									if(minEuc <= 825.164729){
										if(maxX <= 1081.053527){
											if(timeRange <= 1.985){
												if(minEuc <= 727.427628){
													pred_activity = "Bench Press" ;
												}else{
													pred_activity = "Deadlifts" ;}
											}else{
												pred_activity = "Bench Press" ;}
										}else{
											pred_activity = "Shoulder Press" ;}
									}else{
										if(maxY2 <= 71539.594665){
											if(meanZ2 <= 4403.989636){
												pred_activity = "Bench Press" ;
											}else{
												if(maxX <= 1072.798041){
													if(stdDevEuc <= 16.901287){
														if(minY <= -113.612604){
															pred_activity = "No Workout" ;
														}else{
															pred_activity = "Bench Press" ;}
													}else{
														pred_activity = "Bench Press" ;}
												}else{
													if(minZ <= -60.593451){
														pred_activity = "No Workout" ;
													}else{
														pred_activity = "Bench Press" ;}
												}
											}
										}else{
											if(timeRange <= 1.962){
												pred_activity = "Bench Press" ;
											}else{
												pred_activity = "No Workout" ;}
										}
									}
								}else{
									if(stdDevY2 <= 21757.54847){
										if(stdDevZ <= 88.677347){
											if(stdDevX <= 40.286068){
												pred_activity = "Bench Press" ;
											}else{
												if(maxEuc <= 1300.611069){
													pred_activity = "Shoulder Press" ;
												}else{
													pred_activity = "Bench Press" ;}
											}
										}else{
											if(MinJerk <= -606.808778){
												pred_activity = "Bench Press" ;
											}else{
												pred_activity = "No Workout" ;}
										}
									}else{
										if(maxX2 <= 1073483.762327){
											pred_activity = "Flys" ;
										}else{
											if(meanX <= 881.862361){
												pred_activity = "No Workout" ;
											}else{
												pred_activity = "Shoulder Press" ;}
										}
									}
								}
							}else{
								pred_activity = "Shoulder Press" ;}
						}else{
							if(timeRange <= 1.966){
								if(meanX <= -892.155569){
									if(meanX <= -950.003534){
										pred_activity = "Bench Press" ;
									}else{
										pred_activity = "No Workout" ;}
								}else{
									if(minZ <= -61.514798){
										if(minZ <= -133.220576){
											pred_activity = "Shoulder Press" ;
										}else{
											pred_activity = "No Workout" ;}
									}else{
										if(meanY <= -199.105838){
											pred_activity = "Shoulder Press" ;
										}else{
											pred_activity = "Bench Press" ;}
									}
								}
							}else{
								if(minEuc <= 843.781772){
									if(meanY <= -304.659924){
										pred_activity = "Shoulder Press" ;
									}else{
										if(maxX <= 970.856584){
											if(meanEuc2 <= 918978.166776){
												pred_activity = "Shoulder Press" ;
											}else{
												pred_activity = "Bench Press" ;}
										}else{
											pred_activity = "Shoulder Press" ;}
									}
								}else{
									if(maxJerk <= 1939.921785){
										pred_activity = "Shoulder Press" ;
									}else{
										pred_activity = "Bench Press" ;}
								}
							}
						}
					}
				}
			}
		}else{
			if(maxZ <= 569.260622){
				if(minEuc <= 889.702219){
					if(stdDevY2 <= 23942.012248){
						if(maxX <= 1200.617442){
							if(minX2 <= 362134.586689){
								pred_activity = "No Workout" ;
							}else{
								if(minX <= 804.260555){
									pred_activity = "Deadlifts" ;
								}else{
									pred_activity = "No Workout" ;}
							}
						}else{
							if(stdDevX <= 125.954223){
								pred_activity = "Step ups" ;
							}else{
								pred_activity = "Deadlifts" ;}
						}
					}else{
						if(minY <= 500.003397){
							if(meanZ <= 136.674959){
								if(minX <= -880.61583){
									if(stdDevZ <= 82.663507){
										if(maxZ <= 24.617177){
											pred_activity = "Curls" ;
										}else{
											pred_activity = "Walking" ;}
									}else{
										if(minX2 <= 290235.647436){
											if(minZ2 <= 46.01439){
												pred_activity = "Bench Press" ;
											}else{
												pred_activity = "No Workout" ;}
										}else{
											if(timeRange <= 1.977){
												pred_activity = "Pulldowns" ;
											}else{
												pred_activity = "Stairs" ;}
										}
									}
								}else{
									if(meanX <= -455.916656){
										if(stdDevZ2 <= 200146.30171){
											pred_activity = "No Workout" ;
										}else{
											pred_activity = "Situps" ;}
									}else{
										if(maxEuc <= 1344.881882){
											if(meanY <= 747.315998){
												pred_activity = "No Workout" ;
											}else{
												if(stdDevZ <= 99.927295){
													if(timeRange <= 1.973){
														pred_activity = "Pulldowns" ;
													}else{
														pred_activity = "Stairs" ;}
												}else{
													pred_activity = "No Workout" ;}
											}
										}else{
											if(timeRange <= 1.973){
												pred_activity = "Running" ;
											}else{
												if(stdDevX <= 215.653084){
													pred_activity = "Stairs" ;
												}else{
													pred_activity = "No Workout" ;}
											}
										}
									}
								}
							}else{
								if(minX <= 760.11254){
									if(maxX <= 908.673858){
										if(meanX <= -71.107186){
											pred_activity = "Flys" ;
										}else{
											pred_activity = "Curls" ;}
									}else{
										pred_activity = "No Workout" ;}
								}else{
									pred_activity = "Bench Press" ;}
							}
						}else{
							if(stdDevEuc <= 68.937534){
								if(minX <= -645.727523){
									if(timeRange <= 1.965){
										pred_activity = "No Workout" ;
									}else{
										if(meanX <= -403.649418){
											pred_activity = "Stairs" ;
										}else{
											pred_activity = "Pulldowns" ;}
									}
								}else{
									pred_activity = "No Workout" ;}
							}else{
								if(minX2 <= 23449.736626){
									if(meanX2 <= 282255.107707){
										if(stdDevZ <= 96.361085){
											pred_activity = "Pulldowns" ;
										}else{
											pred_activity = "No Workout" ;}
									}else{
										pred_activity = "No Workout" ;}
								}else{
									if(meanZ2 <= 49295.541878){
										pred_activity = "Stairs" ;
									}else{
										pred_activity = "No Workout" ;}
								}
							}
						}
					}
				}else{
					if(minX <= -894.687651){
						if(meanY <= 396.31308){
							if(minY <= 156.270641){
								pred_activity = "Curls" ;
							}else{
								pred_activity = "Stairs" ;}
						}else{
							pred_activity = "Walking" ;}
					}else{
						if(meanEuc <= 1082.404107){
							if(stdDevEuc <= 36.704662){
								if(minZ <= -386.200842){
									if(meanX2 <= 507861.878954){
										pred_activity = "No Workout" ;
									}else{
										if(maxX <= 904.05289){
											if(meanY <= 484.125972){
												if(stdDevY <= 13.435915){
													pred_activity = "Deadlifts" ;
												}else{
													pred_activity = "Pushups" ;}
											}else{
												pred_activity = "No Workout" ;}
										}else{
											pred_activity = "No Workout" ;}
									}
								}else{
									pred_activity = "No Workout" ;}
							}else{
								if(minY <= 310.432723){
									if(stdDevZ <= 28.970672){
										if(stdDevY <= 29.364856){
											pred_activity = "Deadlifts" ;
										}else{
											pred_activity = "No Workout" ;}
									}else{
										if(minZ2 <= 48192.12192){
											if(stdDevEuc <= 90.44075){
												pred_activity = "No Workout" ;
											}else{
												if(timeRange <= 1.98){
													pred_activity = "Deadlifts" ;
												}else{
													pred_activity = "No Workout" ;}
											}
										}else{
											if(maxY <= 323.401717){
												pred_activity = "Deadlifts" ;
											}else{
												if(meanX <= 979.052772){
													pred_activity = "No Workout" ;
												}else{
													pred_activity = "Walking" ;}
											}
										}
									}
								}else{
									if(minX <= 737.736546){
										if(meanZ2 <= 14731.904848){
											pred_activity = "Stairs" ;
										}else{
											if(stdDevX2 <= 13545.129512){
												pred_activity = "Walking" ;
											}else{
												pred_activity = "No Workout" ;}
										}
									}else{
										pred_activity = "Walking" ;}
								}
							}
						}else{
							if(minZ2 <= 51288.62594){
								if(meanZ2 <= 23866.740626){
									if(stdDevX <= 52.491226){
										pred_activity = "Walking" ;
									}else{
										if(meanZ <= -95.627306){
											if(stdDevX <= 87.079932){
												pred_activity = "No Workout" ;
											}else{
												pred_activity = "Walking" ;}
										}else{
											if(meanY2 <= 153001.125507){
												pred_activity = "No Workout" ;
											}else{
												if(minY <= 196.426509){
													pred_activity = "Walking" ;
												}else{
													pred_activity = "No Workout" ;}
											}
										}
									}
								}else{
									if(maxZ <= 163.604315){
										if(maxX <= 1062.34733){
											pred_activity = "Walking" ;
										}else{
											if(meanY2 <= 99228.092664){
												if(meanY <= 294.336739){
													if(meanJerk <= -45.774295){
														pred_activity = "Walking" ;
													}else{
														pred_activity = "No Workout" ;}
												}else{
													pred_activity = "Stairs" ;}
											}else{
												pred_activity = "No Workout" ;}
										}
									}else{
										pred_activity = "Curls" ;}
								}
							}else{
								pred_activity = "Walking" ;}
						}
					}
				}
			}else{
				if(meanY2 <= 381894.146822){
					if(minX2 <= 60679.304601){
						if(minZ <= -224.618442){
							pred_activity = "No Workout" ;
						}else{
							if(stdDevX <= 110.728015){
								pred_activity = "No Workout" ;
							}else{
								pred_activity = "Curls" ;}
						}
					}else{
						if(stdDevY <= 103.974872){
							if(minX <= 261.762767){
								pred_activity = "Curls" ;
							}else{
								pred_activity = "No Workout" ;}
						}else{
							if(maxX <= 1121.525366){
								pred_activity = "Flys" ;
							}else{
								pred_activity = "No Workout" ;}
						}
					}
				}else{
					if(maxY <= 1019.49511){
						if(maxEuc <= 1076.245729){
							pred_activity = "No Workout" ;
						}else{
							if(meanX <= 71.615299){
								pred_activity = "Walking" ;
							}else{
								pred_activity = "No Workout" ;}
						}
					}else{
						if(maxX <= 30.970995){
							pred_activity = "Jump Rope" ;
						}else{
							pred_activity = "Running" ;}
					}
				}
			}
		}
	}else{
		if(minX2 <= 1174287.347485){
			if(stdDevX <= 156.618316){
				pred_activity = "Jump Rope" ;
			}else{
				if(meanY2 <= 1017603.100538){
					if(stdDevY <= 417.684973){
						if(maxZ <= 421.423436){
							pred_activity = "Stairs" ;
						}else{
							pred_activity = "Running" ;}
					}else{
						pred_activity = "No Workout" ;}
				}else{
					pred_activity = "Running" ;}
			}
		}else{
			if(stdDevZ <= 287.179066){
				pred_activity = "No Workout" ;
			}else{
				pred_activity = "Jumping Jacks" ;}
		}
	}


			return pred_activity;
		}

}//end of class